package com.alabuga.shortener.service;

import com.alabuga.shortener.entity.UserAccount;
import com.alabuga.shortener.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAccountRepository users;

    public UserAccount loadOrCreate(Jwt jwt) {
        String sub = jwt.getClaimAsString("sub");
        UUID userId = UUID.fromString(sub);
        
        return users.findById(userId).orElseGet(() -> {
            UserAccount user = buildUserFromJwt(jwt, userId);
            return users.save(user);
        });
    }

    private UserAccount buildUserFromJwt(Jwt jwt, UUID userId) {
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        String givenName = jwt.getClaimAsString("given_name");
        String familyName = jwt.getClaimAsString("family_name");
        String middleName = jwt.getClaimAsString("middle_name");
        String fullName = jwt.getClaimAsString("name");
        String phoneNumber = jwt.getClaimAsString("phone_number");
        Boolean phoneNumberVerified = jwt.getClaimAsBoolean("phone_number_verified");
        Boolean emailVerified = jwt.getClaimAsBoolean("email_verified");
        String gender = jwt.getClaimAsString("gender");
        
        return UserAccount.builder()
                .id(userId)
                .username(username != null ? username : "unknown")
                .email(email != null ? email : "unknown@local")
                .givenName(givenName)
                .familyName(familyName)
                .middleName(middleName)
                .fullName(fullName)
                .phoneNumber(phoneNumber)
                .phoneNumberVerified(phoneNumberVerified)
                .emailVerified(emailVerified)
                .gender(gender)
                .build();
    }

    public Optional<UserAccount> findById(UUID userId) {
        return users.findById(userId);
    }
}