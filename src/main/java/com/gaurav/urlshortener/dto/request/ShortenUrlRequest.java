package com.gaurav.urlshortener.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortenUrlRequest {

    @Pattern(
            regexp = "^(https?://).+",
            message = "Invalid URL"
    )
    private String url;

    @Size(min = 3, max = 20)
    private String customAlias;
}