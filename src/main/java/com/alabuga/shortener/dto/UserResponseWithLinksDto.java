package com.alabuga.shortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseWithLinksDto {
    
    private UserAccountDto user;
    private List<ShortLinkDto> links;
    private Integer totalLinks;
}
