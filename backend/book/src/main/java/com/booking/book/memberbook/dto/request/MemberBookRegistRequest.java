package com.booking.book.memberbook.dto.request;

public record MemberBookRegistRequest(
    Integer memberPk,
    String bookIsbn
) {

}
