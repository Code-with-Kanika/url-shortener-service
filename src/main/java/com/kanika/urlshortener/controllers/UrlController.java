package com.kanika.urlshortener.controllers;

import java.net.MalformedURLException;
import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kanika.urlshortener.service.UrlService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestBody String longUrl) throws MalformedURLException {

        String code = urlService.shortenUrl(longUrl);

        return ResponseEntity.ok(code);
    }

     // For Browser testing
    @GetMapping("/shorten")
    public ResponseEntity<String> shortenFromBrowser(@RequestParam String url) throws MalformedURLException {

        String code = urlService.shortenUrl(url);
        return ResponseEntity.ok(code);
    }

    @GetMapping("/{code:[a-zA-Z0-9]{6}}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {

        String longUrl = urlService.getLongUrl(code);

        return ResponseEntity
                .status(302)
                .location(URI.create(longUrl))
                .build();
    }
}
