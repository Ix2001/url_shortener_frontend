package com.alabuga.shortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkDto {
    
    private Long id;
    private String code;
    private String shortUrl;
    private String originalUrl;
    private LocalDateTime createdAt;
    private Long clickCount;
    private LocalDateTime lastAccessed;
    private UUID userId;
    private String username;
}
