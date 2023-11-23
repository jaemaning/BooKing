package com.booking.book.book.exception;

import com.booking.book.global.exception.ErrorCode;
import com.booking.book.global.exception.GlobalException;

public class BookException extends GlobalException {

    public BookException(ErrorCode errorCode) {
        super(errorCode);
    }
}
