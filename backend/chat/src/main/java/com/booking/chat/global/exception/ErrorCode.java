package com.booking.chat.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 채팅방입니다."),
    MEMBER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 가입한 채팅방입니다."),
    MEMBER_NOT_PART_OF_CHATROOM(HttpStatus.BAD_REQUEST , "가입하지 않은 채팅방입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
