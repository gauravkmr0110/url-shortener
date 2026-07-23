package com.gaurav.urlshortener.controller;

import com.gaurav.urlshortener.dto.request.ShortenUrlRequest;
import com.gaurav.urlshortener.dto.response.ShortenUrlResponse;
import com.gaurav.urlshortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(
            @Valid @RequestBody ShortenUrlRequest request) {

        ShortenUrlResponse response = urlShortenerService.shortenUrl(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {

        String originalUrl = urlShortenerService.getOriginalUrl(code);

        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create(originalUrl))
                .build();
    }
}