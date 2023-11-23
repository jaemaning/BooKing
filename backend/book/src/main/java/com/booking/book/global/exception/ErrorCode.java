package com.booking.book.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 책입니다."),
    MEMBER_NOT_READ(HttpStatus.BAD_REQUEST, "읽지 않은 책입니다."),
    BOOK_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "이미 읽은 책입니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
