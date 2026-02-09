package com.kanika.urlshortener.controllers;

import com.kanika.urlshortener.service.UrlService;
import lombok.RequiredArgsConstructor;

import java.net.MalformedURLException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UrlService urlService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

   @PostMapping("/shorten-ui")
public String shorten(@RequestParam String longUrl, Model model) throws MalformedURLException {

    String code = urlService.shortenUrl(longUrl);

    String shortUrl = "/" + code;

    model.addAttribute("shortUrl", shortUrl);

    return "index";
}

}

