package com.alabuga.shortener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDto {
    
    private UUID id;
    private String username;
    private String email;
    private String givenName;
    private String familyName;
    private String middleName;
    private String fullName;
    private String phoneNumber;
    private Boolean phoneNumberVerified;
    private Boolean emailVerified;
    private String gender;
}
