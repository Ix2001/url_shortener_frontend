package com.alabuga.shortener.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccount {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id; // sub из Keycloak как UUID

    @Column(nullable = false, length = 50)
    private String username; // preferred_username

    @Column(nullable = false, length = 255)
    private String email; // email из JWT

    @Column(length = 100)
    private String givenName; // given_name

    @Column(length = 100)
    private String familyName; // family_name

    @Column(length = 100)
    private String middleName; // middle_name

    @Column(length = 255)
    private String fullName; // name

    @Column(length = 20)
    private String phoneNumber; // phone_number

    @Column
    private Boolean phoneNumberVerified; // phone_number_verified

    @Column
    private Boolean emailVerified; // email_verified

    @Column(length = 10)
    private String gender; // gender

    // Обратная связь к ссылкам пользователя
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShortLink> shortLinks;
}