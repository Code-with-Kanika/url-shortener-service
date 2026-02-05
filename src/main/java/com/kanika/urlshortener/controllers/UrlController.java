package com.kanika.urlshortener.controllers;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kanika.urlshortener.service.UrlService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestBody String longUrl) {

        String code = urlService.shortenUrl(longUrl);

        return ResponseEntity.ok(code);
    }

     // For Browser testing
    @GetMapping("/shorten")
    public ResponseEntity<String> shortenFromBrowser(@RequestParam String url) {

        String code = urlService.shortenUrl(url);
        return ResponseEntity.ok(code);
    }

    @GetMapping("/{code}")
    public void redirect(@PathVariable String code,
                         HttpServletResponse response) throws IOException {

        String longUrl = urlService.getLongUrl(code);

        response.sendRedirect(longUrl);
    }
}
