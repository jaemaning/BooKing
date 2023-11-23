package com.booking.book.memberbook.domain;

import java.time.LocalDateTime;

public record Note(
    String memo,
    LocalDateTime createdAt
) {

}
