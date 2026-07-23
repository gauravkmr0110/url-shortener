package com.gaurav.urlshortener.service.impl;

import com.gaurav.urlshortener.dto.request.ShortenUrlRequest;
import com.gaurav.urlshortener.dto.response.ShortenUrlResponse;
import com.gaurav.urlshortener.entity.UrlMapping;
import com.gaurav.urlshortener.exception.AliasAlreadyExistsException;
import com.gaurav.urlshortener.exception.UrlNotFoundException;
import com.gaurav.urlshortener.repository.UrlRepository;
import com.gaurav.urlshortener.util.Base62Generator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UrlShortenerServiceImplTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private Base62Generator base62Generator;

    @InjectMocks
    private UrlShortenerServiceImpl urlShortenerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateShortUrl() {

        ShortenUrlRequest request = new ShortenUrlRequest();
        request.setUrl("https://www.google.com");

        when(urlRepository.findByOriginalUrl(request.getUrl()))
                .thenReturn(Optional.empty());

        when(base62Generator.generateShortCode())
                .thenReturn("abc1234");

        when(urlRepository.existsByShortCode("abc1234"))
                .thenReturn(false);

        when(urlRepository.save(any(UrlMapping.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ShortenUrlResponse response = urlShortenerService.shortenUrl(request);
        assertEquals("abc1234", response.getShortCode());
        verify(urlRepository).save(any(UrlMapping.class));
    }

    @Test
    void shouldReturnExistingShortUrl() {

        ShortenUrlRequest request = new ShortenUrlRequest();
        request.setUrl("https://www.google.com");

        UrlMapping existingMapping = UrlMapping.builder()
                .originalUrl("https://www.google.com")
                .shortCode("abc1234")
                .build();

        when(urlRepository.findByOriginalUrl(request.getUrl()))
                .thenReturn(Optional.of(existingMapping));

        ShortenUrlResponse response = urlShortenerService.shortenUrl(request);

        assertEquals("abc1234", response.getShortCode());

        verify(urlRepository, never()).save(any(UrlMapping.class));
        verify(base62Generator, never()).generateShortCode();
    }

    @Test
    void shouldThrowExceptionForDuplicateAlias() {

        ShortenUrlRequest request = new ShortenUrlRequest();
        request.setUrl("https://www.facebook.com");
        request.setCustomAlias("google");

        when(urlRepository.findByOriginalUrl(request.getUrl()))
                .thenReturn(Optional.empty());

        when(urlRepository.existsByShortCode("google"))
                .thenReturn(true);

        AliasAlreadyExistsException exception = assertThrows(
                AliasAlreadyExistsException.class,
                () -> urlShortenerService.shortenUrl(request)
        );

        assertEquals("Custom alias already exists", exception.getMessage());

        verify(urlRepository, never()).save(any(UrlMapping.class));
    }

    @Test
    void shouldReturnOriginalUrl() {

        UrlMapping urlMapping = UrlMapping.builder()
                .originalUrl("https://www.google.com")
                .shortCode("abc1234")
                .build();

        when(urlRepository.findByShortCode("abc1234"))
                .thenReturn(Optional.of(urlMapping));

        String originalUrl = urlShortenerService.getOriginalUrl("abc1234");

        assertEquals("https://www.google.com", originalUrl);

        verify(urlRepository).findByShortCode("abc1234");
    }

    @Test
    void shouldThrowUrlNotFoundException() {

        when(urlRepository.findByShortCode("invalid123"))
                .thenReturn(Optional.empty());

        UrlNotFoundException exception = assertThrows(
                UrlNotFoundException.class,
                () -> urlShortenerService.getOriginalUrl("invalid123")
        );

        assertEquals("Short URL not found", exception.getMessage());

        verify(urlRepository).findByShortCode("invalid123");
    }

}