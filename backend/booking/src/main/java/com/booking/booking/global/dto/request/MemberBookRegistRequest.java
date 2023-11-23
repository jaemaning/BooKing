package com.booking.booking.global.dto.request;

public record MemberBookRegistRequest(
        Integer memberPk,
        String bookIsbn
) {
}
