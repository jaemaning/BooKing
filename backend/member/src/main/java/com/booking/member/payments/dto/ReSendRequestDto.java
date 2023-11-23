package com.booking.member.payments.dto;

public record ReSendRequestDto(
        Integer receiverMemberPk,
        Integer amount
) {
}
