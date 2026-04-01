package com.hayeon.reservation.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class AdminHtmlController {

    // Vercel/프로덕션에서 호출할 엔드포인트를 환경변수로 주입합니다.
    // 예) https://group-reservation-lime.vercel.app/stores/store-1
    @Value("${RESERVATION_LIST_URL:/api/reservations/admin/list}")
    private String reservationListUrl;

    @GetMapping(value = "/admin.html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> adminHtml() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/admin.html");
        byte[] bytes = resource.getInputStream().readAllBytes();
        String html = new String(bytes, StandardCharsets.UTF_8);

        // 프론트의 "__RESERVATION_LIST_URL__" 토큰을 치환합니다.
        html = html.replace("__RESERVATION_LIST_URL__", reservationListUrl);
        return ResponseEntity.ok(html);
    }
}

