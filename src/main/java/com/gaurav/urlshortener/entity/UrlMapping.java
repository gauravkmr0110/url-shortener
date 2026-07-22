package com.gaurav.urlshortener.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "url_mapping",
        indexes = {
                @Index(name = "idx_short_code", columnList = "short_code")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_url", nullable = false, length = 2048)
    private String originalUrl;

    @Column(name = "short_code", nullable = false, unique = true, length = 20)
    private String shortCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}