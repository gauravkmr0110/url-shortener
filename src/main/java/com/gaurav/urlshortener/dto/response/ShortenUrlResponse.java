package com.gaurav.urlshortener.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ShortenUrlResponse {

    private String shortCode;

    private String shortUrl;
}