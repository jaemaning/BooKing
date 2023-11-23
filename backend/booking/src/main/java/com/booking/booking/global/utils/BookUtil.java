package com.booking.booking.global.utils;

import com.booking.booking.global.dto.request.MemberBookRegistRequest;
import com.booking.booking.global.dto.response.BookResponse;
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
public class BookUtil {
    private static String GATEWAY_URL;
    private final static String AUTHORIZATION = "Authorization";

    @Value("${gateway.url}")
    public void setGatewayUrl(String gatewayUrl) {
        BookUtil.GATEWAY_URL = gatewayUrl;
    }

    public static Mono<BookResponse> getBookByIsbn(String isbn) {
        log.info("[Booking:BookUtil] getBookByIsbn({})", isbn);

        WebClient webClient = WebClient.builder().build();
        URI uri = URI.create(GATEWAY_URL + "/api/book/searchByIsbn?isbn=" + isbn);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(new RuntimeException("책 정보 응답 에러")))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new RuntimeException("책 정보 응답 에러")))
                .bodyToMono(BookResponse.class);
    }

    public static Mono<Void> increaseMeetingCount(String isbn) {
        log.info("[Booking:BookUtil] increaseMeetingCount({})", isbn);

        WebClient webClient = WebClient.builder().build();
        URI uri = URI.create(GATEWAY_URL + "/api/book/increment/" + isbn);

        return webClient.post()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(new RuntimeException("책 증가 에러")))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new RuntimeException("책 증가 에러")))
                .bodyToMono(Void.class);
    }

    public static Mono<Void> registerMemberBook(String token, MemberBookRegistRequest memberBookRegistRequest) {
        log.info("[Booking:BookUtil] registerMemberBook({}, {})", token, memberBookRegistRequest);

        WebClient webClient = WebClient.builder().build();
        URI uri = URI.create(GATEWAY_URL + "/api/book/member/");

        return webClient.post()
                .uri(uri)
                .header(AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(memberBookRegistRequest), MemberBookRegistRequest.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(new RuntimeException("책 등록 에러")))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new RuntimeException("책 등록 에러")))
                .bodyToMono(Void.class);
    }
}
