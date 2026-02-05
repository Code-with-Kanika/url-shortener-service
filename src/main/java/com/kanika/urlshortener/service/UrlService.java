package com.kanika.urlshortener.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.kanika.urlshortener.entity.UrlMapping;
import com.kanika.urlshortener.repository.UrlRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UrlService {
     private final UrlRepository urlRepository;

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;

    public String shortenUrl(String longUrl) {

        String shortCode = generateUniqueShortCode(longUrl);

        UrlMapping mapping = new UrlMapping();
        mapping.setShortCode(shortCode);
        mapping.setLongUrl(longUrl);

        urlRepository.save(mapping);
        return shortCode;
    }
    private String generateUniqueShortCode(String longUrl) {
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

    public String getLongUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .map(UrlMapping::getLongUrl)
                .orElseThrow(() -> new RuntimeException("URL not found"));
    }
}
