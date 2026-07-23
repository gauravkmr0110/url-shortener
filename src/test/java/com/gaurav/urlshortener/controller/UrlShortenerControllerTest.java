package com.gaurav.urlshortener.controller;

import com.gaurav.urlshortener.dto.request.ShortenUrlRequest;
import com.gaurav.urlshortener.dto.response.ShortenUrlResponse;
import com.gaurav.urlshortener.service.UrlShortenerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlShortenerController.class)
class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlShortenerService urlShortenerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldShortenUrl() throws Exception {

        ShortenUrlRequest request = new ShortenUrlRequest();
        request.setUrl("https://www.google.com");

        ShortenUrlResponse response = ShortenUrlResponse.builder()
                .shortCode("abc1234")
                .shortUrl("http://localhost:8080/abc1234")
                .build();

        when(urlShortenerService.shortenUrl(any(ShortenUrlRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortCode").value("abc1234"))
                .andExpect(jsonPath("$.shortUrl").value("http://localhost:8080/abc1234"));
    }

    @Test
    void shouldRedirectToOriginalUrl() throws Exception {

        when(urlShortenerService.getOriginalUrl("abc1234"))
                .thenReturn("https://www.google.com");

        mockMvc.perform(get("/abc1234"))
                .andExpect(status().isMovedPermanently())
                .andExpect(header().string("Location", "https://www.google.com"));
    }
}