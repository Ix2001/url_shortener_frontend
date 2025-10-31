package com.alabuga.shortener.controller;

import com.alabuga.shortener.dto.*;
import com.alabuga.shortener.entity.ShortLink;
import com.alabuga.shortener.entity.UserAccount;
// import com.alabuga.shortener.mapper.ShortLinkMapper;
import com.alabuga.shortener.service.ShortLinkService;
import com.alabuga.shortener.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Short Link API", description = "API для управления короткими ссылками")
@SecurityRequirement(name = "bearerAuth")
public class ShortLinkApi {

    private final ShortLinkService shortLinkService;
    private final UserService userService;
    // private final ShortLinkMapper shortLinkMapper;

    @PostMapping("/api/shorten")
    @Operation(summary = "Создать короткую ссылку", description = "Создает короткую ссылку для указанного URL")
    public ResponseEntity<CreateShortLinkResponse> create(@Valid @RequestBody CreateShortLinkRequest request,
                                                          @AuthenticationPrincipal Jwt jwt) {
        UserAccount user = userService.loadOrCreate(jwt);
        String code = shortLinkService.createShort(request.getUrl(), user);
        
        CreateShortLinkResponse response = CreateShortLinkResponse.builder()
                .shortUrl("/" + code)
                .originalUrl(request.getUrl())
                .build();
                
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/my-links")
    @Operation(summary = "Получить мои ссылки", description = "Возвращает список всех коротких ссылок текущего пользователя со статистикой")
    public ResponseEntity<UserResponseWithLinksDto> myLinks(@AuthenticationPrincipal Jwt jwt) {
        UserAccount user = userService.loadOrCreate(jwt);
        List<ShortLink> links = shortLinkService.myLinks(user);
        List<ShortLinkDto> linkDtos = (links != null ? links : List.<ShortLink>of()).stream()
                .map(this::toDto)
                .toList();
        
        UserAccountDto userDto = toUserDto(user);
        
        UserResponseWithLinksDto response = UserResponseWithLinksDto.builder()
                .user(userDto)
                .links(linkDtos)
                .totalLinks(links != null ? links.size() : 0)
                .build();
        
        return ResponseEntity.ok(response);
    }


    @GetMapping("/api/stats/{code}")
    @Operation(summary = "Статистика ссылки", description = "Возвращает статистику по указанной короткой ссылке (только для владельца)")
    public ResponseEntity<ShortLinkStatsDto> stats(@PathVariable String code,
                                                   @AuthenticationPrincipal Jwt jwt) {
        UserAccount user = userService.loadOrCreate(jwt);
        
        return shortLinkService.findByCode(code)
                .filter(link -> link.getUser().getId().equals(user.getId())) // Проверяем, что ссылка принадлежит текущему пользователю
                .map(link -> {
                    ShortLinkStatsDto statsDto = toStatsDto(link);
                    return ResponseEntity.ok(statsDto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/links/{code}")
    @Operation(summary = "Удалить ссылку", description = "Удаляет короткую ссылку (только для владельца)")
    public ResponseEntity<DeleteLinkResponse> deleteLink(@PathVariable String code,
                                                         @AuthenticationPrincipal Jwt jwt) {
        UserAccount user = userService.loadOrCreate(jwt);
        
        return shortLinkService.findByCode(code)
                .filter(link -> link.getUser().getId().equals(user.getId())) // Проверяем, что ссылка принадлежит текущему пользователю
                .map(link -> {
                    shortLinkService.deleteLink(link);
                    DeleteLinkResponse response = DeleteLinkResponse.builder()
                            .message("Ссылка успешно удалена")
                            .code(code)
                            .build();
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private ShortLinkDto toDto(ShortLink shortLink) {
        return ShortLinkDto.builder()
                .id(shortLink.getId())
                .code(shortLink.getCode())
                .shortUrl("/" + shortLink.getCode())
                .originalUrl(shortLink.getOriginalUrl())
                .userId(shortLink.getUser().getId())
                .username(shortLink.getUser().getUsername())
                .createdAt(shortLink.getCreatedAt())
                .clickCount(shortLink.getClickCount())
                .lastAccessed(shortLink.getLastAccessed())
                .build();
    }

    private ShortLinkStatsDto toStatsDto(ShortLink shortLink) {
        return ShortLinkStatsDto.builder()
                .code(shortLink.getCode())
                .shortUrl("/" + shortLink.getCode())
                .originalUrl(shortLink.getOriginalUrl())
                .ownerId(shortLink.getUser().getId())
                .ownerUsername(shortLink.getUser().getUsername())
                .clickCount(shortLink.getClickCount())
                .createdAt(shortLink.getCreatedAt())
                .lastAccessed(shortLink.getLastAccessed())
                .build();
    }

    private UserAccountDto toUserDto(UserAccount user) {
        return UserAccountDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .givenName(user.getGivenName())
                .familyName(user.getFamilyName())
                .middleName(user.getMiddleName())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .phoneNumberVerified(user.getPhoneNumberVerified())
                .emailVerified(user.getEmailVerified())
                .gender(user.getGender())
                .build();
    }
}