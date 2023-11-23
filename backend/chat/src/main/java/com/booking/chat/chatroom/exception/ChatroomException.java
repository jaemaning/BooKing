package com.booking.chat.chatroom.exception;

import com.booking.chat.global.exception.ErrorCode;
import com.booking.chat.global.exception.GlobalException;

public class ChatroomException extends GlobalException {
    public ChatroomException(ErrorCode errorCode) {
        super(errorCode);
    }
}
