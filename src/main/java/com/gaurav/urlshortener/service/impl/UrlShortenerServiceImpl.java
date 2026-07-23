package com.gaurav.urlshortener.service.impl;

import com.gaurav.urlshortener.dto.request.ShortenUrlRequest;
import com.gaurav.urlshortener.dto.response.ShortenUrlResponse;
import com.gaurav.urlshortener.entity.UrlMapping;
import com.gaurav.urlshortener.repository.UrlRepository;
import com.gaurav.urlshortener.service.UrlShortenerService;
import com.gaurav.urlshortener.util.Base62Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlShortenerServiceImpl implements UrlShortenerService {

    private final UrlRepository urlRepository;
    private final Base62Generator base62Generator;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public ShortenUrlResponse shortenUrl(ShortenUrlRequest request) {

        // Check if URL is already shortened
        UrlMapping existingMapping = urlRepository
                .findByOriginalUrl(request.getUrl())
                .orElse(null);

        if (existingMapping != null) {
            return buildResponse(existingMapping);
        }

        // Generate unique short code
        String shortCode;
        do {
            shortCode = base62Generator.generateShortCode();
        } while (urlRepository.existsByShortCode(shortCode));

        // Create entity
        UrlMapping urlMapping = UrlMapping.builder()
                .originalUrl(request.getUrl())
                .shortCode(shortCode)
                .createdAt(LocalDateTime.now())
                .build();

        // Save to database
        urlRepository.save(urlMapping);

        return buildResponse(urlMapping);
    }

    @Override
    public String getOriginalUrl(String shortCode) {

        UrlMapping urlMapping = urlRepository
                .findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found"));

        return urlMapping.getOriginalUrl();
    }

    private ShortenUrlResponse buildResponse(UrlMapping urlMapping) {

        return ShortenUrlResponse.builder()
                .shortCode(urlMapping.getShortCode())
                .shortUrl(baseUrl + "/" + urlMapping.getShortCode())
                .build();
    }
}