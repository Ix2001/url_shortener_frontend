package com.alabuga.shortener.service;

import com.alabuga.shortener.entity.ShortLink;
import com.alabuga.shortener.entity.UserAccount;
import com.alabuga.shortener.repository.ShortLinkRepository;
import com.alabuga.shortener.util.RandomCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShortLinkService {

    private final ShortLinkRepository shortLinkRepository;

    public String createShort(String originalUrl, UserAccount user) {
        String code = generateUniqueCode();
        
        ShortLink link = shortLinkRepository.save(
                ShortLink.builder()
                        .code(code)
                        .originalUrl(originalUrl)
                        .createdAt(LocalDateTime.now())
                        .clickCount(0L)
                        .user(user)
                        .build()
        );
        return code;
    }

    public Optional<ShortLink> findByCode(String code) {
        return shortLinkRepository.findByCode(code);
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = RandomCodeGenerator.generateRandomCode();
        } while (shortLinkRepository.existsByCode(code));
        return code;
    }

    public void incrementClick(ShortLink link) {
        link.setClickCount(link.getClickCount() + 1);
        link.setLastAccessed(LocalDateTime.now());
        shortLinkRepository.save(link);
    }

    public List<ShortLink> myLinks(UUID userId) {
        return shortLinkRepository.findByUserId(userId);
    }

    // Альтернативный метод через обратную связь (если у вас есть UserAccount)
    public List<ShortLink> myLinks(UserAccount user) {
        List<ShortLink> links = user.getShortLinks();
        return links != null ? links : List.of();
    }

    public void deleteLink(ShortLink link) {
        shortLinkRepository.delete(link);
    }

    public void cleanupOld() {
        shortLinkRepository.deleteOldLinks(LocalDateTime.now().minusDays(7));
    }
}