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
public class ShortLinkStatsDto {
    
    private String code;
    private String shortUrl;
    private String originalUrl;
    private Long clickCount;
    private LocalDateTime createdAt;
    private LocalDateTime lastAccessed;
    private UUID ownerId;
    private String ownerUsername;
}
