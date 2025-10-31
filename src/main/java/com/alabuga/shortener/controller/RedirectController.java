package com.alabuga.shortener.controller;

import com.alabuga.shortener.entity.ShortLink;
import com.alabuga.shortener.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class RedirectController {

    private final ShortLinkService shortLinkService;

    @GetMapping("/api/{code}")
    public void redirect(@PathVariable String code, HttpServletResponse response) throws IOException {
        Optional<ShortLink> linkOpt = shortLinkService.findByCode(code);
        if (linkOpt.isEmpty()) {
            response.sendError(HttpStatus.NOT_FOUND.value(), "Short URL not found");
            return;
        }
        ShortLink link = linkOpt.get();
        shortLinkService.incrementClick(link);
        response.sendRedirect(link.getOriginalUrl());
    }
}