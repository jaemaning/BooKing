package com.booking.booking.global.dto.request;

public record PaymentRequest(
        String receiver,
        Integer amount
) {
}
