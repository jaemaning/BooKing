package com.booking.booking.global.utils;

import com.booking.booking.global.dto.request.PaymentRequest;
import com.booking.booking.global.dto.request.ReSendRequest;
import com.booking.booking.global.dto.response.MemberResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Slf4j
@Component
public class MemberUtil {
    private final WebClient webClient;
    private final String AUTHORIZATION = "Authorization";
    private final String RECIEVER = "kakao_3144707067";

    public MemberUtil(@Value("${gateway.url}") String gatewayUrl) {
        ConnectionProvider provider = ConnectionProvider.builder("ApiConnections")
                .maxConnections(16)
                .maxIdleTime(Duration.ofSeconds(30))
                .maxLifeTime(Duration.ofSeconds(60))
                .pendingAcquireTimeout(Duration.ofSeconds(60))
                .evictInBackground(Duration.ofSeconds(120))
                .build();

        this.webClient = WebClient.builder()
                .baseUrl(gatewayUrl)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create(provider)))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<MemberResponse> getMemberInfoByEmail(String userEmail) {
        log.info("[Booking:MemberUtil] getMemberInfoByEmail({})", userEmail);
        return makeGetRequest("/api/members/memberInfo/" + userEmail);
    }

    public Mono<MemberResponse> getMemberInfoByPk(Integer memberPk) {
        log.info("[Booking:MemberUtil] getMemberInfoByPk({})", memberPk);
        return makeGetRequest("/api/members/memberInfo-pk/" + memberPk);
    }

    private Mono<MemberResponse> makeGetRequest(String path) {
        return webClient.get()
                        .uri(path)
                        .retrieve()
                        .onStatus(HttpStatus::is4xxClientError,
                            response -> Mono.error(new RuntimeException("회원정보 응답 에러")))
                        .onStatus(HttpStatus::is5xxServerError,
                            response -> Mono.error(new RuntimeException("회원정보 응답 에러")))
                        .bodyToMono(MemberResponse.class);
    }

    public Mono<String> payRequest(String token, Integer fee) {
        log.info("[Booking:MemberUtil] payRequest({}, {})", token, fee);

        return webClient.post()
                .uri("/api/payments/send")
                .header(AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new PaymentRequest(RECIEVER, fee)), PaymentRequest.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException(errorBody))))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new RuntimeException("참가비 응답 에러")))
                .bodyToMono(String.class);
    }

    public Mono<String> paybackRequest(Integer memberId, Integer amount) {
        log.info("[Booking:MemberUtil] paybackRequest({}, {})", memberId, amount);

        return webClient.post()
                .uri("/api/payments/resend")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new ReSendRequest(memberId, amount)), ReSendRequest.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                        .flatMap(errorBody -> Mono.error(new RuntimeException(errorBody))))
                .onStatus(HttpStatus::is5xxServerError,
                        response -> Mono.error(new RuntimeException("참가비 응답 에러")))
                .bodyToMono(String.class);
    }
}
