package com.alabuga.shortener.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateShortLinkRequest {
    
    @NotBlank(message = "URL не может быть пустым")
    private String url;
}
