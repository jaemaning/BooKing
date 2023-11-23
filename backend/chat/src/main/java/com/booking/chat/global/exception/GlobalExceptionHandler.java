package com.booking.chat.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public Mono<ResponseEntity<String>> handleGlobalException(GlobalException globalException) {
        ErrorCode errorCode = globalException.errorCode;
        ErrorResponse errorResponse = ErrorResponse.from(globalException);

        log.error("Error occurred: {} - {}", errorResponse.httpStatus(), errorResponse.message(), globalException);

        return Mono.just(ResponseEntity.status(errorResponse.httpStatus()).body(errorResponse.message()));
    }
}
