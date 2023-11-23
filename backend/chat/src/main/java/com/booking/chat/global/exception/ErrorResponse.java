package com.booking.chat.global.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
    HttpStatus httpStatus,
    String message
) {
    public static ErrorResponse from(GlobalException globalException) {
        return new ErrorResponse(globalException.errorCode.getHttpStatus(), globalException.errorCode.getErrorMessage());
    }
}
