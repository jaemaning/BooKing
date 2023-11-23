package com.booking.booking.global.utils;

import com.booking.booking.global.dto.request.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Component
public class NotificationUtil {
    private static String GATEWAY_URL;

    @Value("${gateway.url}")
    public void setGatewayUrl(String gatewayUrl) {
        NotificationUtil.GATEWAY_URL = gatewayUrl;
    }

    public static Mono<Void> sendNotification(NotificationRequest notificationRequest) {
        log.info("[Booking:NotificationUtil] sendNotification({})", notificationRequest);

        WebClient webClient = WebClient.builder().build();
        URI uri = URI.create(GATEWAY_URL + "/api/notification/");

        return webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(notificationRequest), NotificationRequest.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(new RuntimeException("알람 응답 에러")))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new RuntimeException("알람 응답 에러")))
                .bodyToMono(Void.class);
    }
}
