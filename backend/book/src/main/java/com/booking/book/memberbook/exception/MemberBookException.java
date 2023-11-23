package com.booking.book.memberbook.exception;

import com.booking.book.global.exception.ErrorCode;
import com.booking.book.global.exception.GlobalException;

public class MemberBookException extends GlobalException {

    public MemberBookException(ErrorCode errorCode) {
        super(errorCode);
    }
}
