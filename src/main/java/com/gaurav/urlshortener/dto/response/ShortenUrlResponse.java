package com.gaurav.urlshortener.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShortenUrlResponse {

    private String shortCode;

    private String shortUrl;
}