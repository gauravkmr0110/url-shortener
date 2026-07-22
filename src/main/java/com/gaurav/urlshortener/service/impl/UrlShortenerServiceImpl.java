package com.gaurav.urlshortener.service.impl;

import com.gaurav.urlshortener.dto.request.ShortenUrlRequest;
import com.gaurav.urlshortener.dto.response.ShortenUrlResponse;
import com.gaurav.urlshortener.repository.UrlRepository;
import com.gaurav.urlshortener.service.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlShortenerServiceImpl implements UrlShortenerService {

    private final UrlRepository urlRepository;

    @Override
    public ShortenUrlResponse shortenUrl(ShortenUrlRequest request) {

        // TODO:
        // Check duplicate URL
        // Handle custom alias
        // Generate short code
        // Save entity

        return null;
    }

    @Override
    public String getOriginalUrl(String shortCode) {

        // TODO:
        // Find mapping
        // Throw exception if not found

        return null;
    }
}