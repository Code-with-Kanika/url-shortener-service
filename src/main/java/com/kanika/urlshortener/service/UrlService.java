package com.kanika.urlshortener.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.kanika.urlshortener.entity.UrlMapping;
import com.kanika.urlshortener.repository.UrlRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;
    private final Counter redirectCounter;
    private final Counter shortenCounter;

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;

    @Autowired
    public UrlService(UrlRepository urlRepository,
                      MeterRegistry meterRegistry) {

        this.urlRepository = urlRepository;

        this.redirectCounter = meterRegistry.counter("url.redirect.count");
        this.shortenCounter = meterRegistry.counter("url.shorten.count");
    }
    public String shortenUrl(String longUrl) {
        shortenCounter.increment();
        String finalUrl = longUrl.trim();

        if (!finalUrl.startsWith("http")) {
            finalUrl = "https://" + finalUrl;
        }
        final String normalizedUrl = finalUrl;
       
        return urlRepository.findByLongUrl(normalizedUrl)
            .map(UrlMapping::getShortCode) // If exists → return old code
            .orElseGet(() -> {
                String shortCode = generateUniqueShortCode();

                UrlMapping mapping = new UrlMapping();
                mapping.setShortCode(shortCode);
                mapping.setLongUrl(normalizedUrl);

                urlRepository.save(mapping);
                return shortCode;
            });
    }
    
    private String generateUniqueShortCode() {
        String shortCode;
        int maxLimit =5 ;
        int count=0;

        do {
            shortCode = generateShortCode();
            count++;
        } while (count< maxLimit && urlRepository.existsByShortCode(shortCode));
        
        if (count >= maxLimit) {
            throw new RuntimeException("Failed to generate unique short code");
        }
        return shortCode;
    }
    private String generateShortCode() {

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(BASE62.charAt(random.nextInt(BASE62.length())));
        }

        return sb.toString();
    }

    @Cacheable(value = "urls", key = "#shortCode")
    public String getLongUrl(String shortCode) {
        System.out.println("Fetching from DB...");
        redirectCounter.increment();
        return urlRepository.findByShortCode(shortCode)
                .map(UrlMapping::getLongUrl)
                .orElseThrow(() -> new IllegalArgumentException("Invalid short URL"));
    }
}
