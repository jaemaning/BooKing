package com.booking.member.payments.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReadyPaymentResponseDto(
        String tid,
        String next_redirect_app_url,
        String next_redirect_mobile_url,
        String next_redirect_pc_url,
        String android_app_scheme,
        String ios_app_scheme,
        LocalDateTime created_at
) {
}
