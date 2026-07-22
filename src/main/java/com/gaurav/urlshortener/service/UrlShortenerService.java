package com.gaurav.urlshortener.service;

import com.gaurav.urlshortener.dto.request.ShortenUrlRequest;
import com.gaurav.urlshortener.dto.response.ShortenUrlResponse;

public interface UrlShortenerService {

    ShortenUrlResponse shortenUrl(ShortenUrlRequest request);

    String getOriginalUrl(String shortCode);
}