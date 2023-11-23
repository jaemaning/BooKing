package com.booking.member.payments.dto;

import lombok.Builder;

@Builder
public record SendRequestDto(
        String receiver,
        Integer amount
) {
}
