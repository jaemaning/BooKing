package com.booking.booking.global.dto.request;

public record ReSendRequest(
        Integer receiverMemberPk,
        Integer amount
) {
}
