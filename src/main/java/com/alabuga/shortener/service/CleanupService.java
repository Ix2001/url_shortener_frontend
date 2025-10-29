package com.alabuga.shortener.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CleanupService {

    private final ShortLinkService shortLinkService;

    // каждый день в 03:00 по серверному времени
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanup() {
        shortLinkService.cleanupOld();
        log.info("Cleanup finished (TTL=7d)");
    }
}