package com.booking.booking.global.dto.response;

import java.time.LocalDate;

public record BookResponse(
        String title,
        String author,
        String coverImage,
        String genre,
        LocalDate publishDate,
        String content,
        String isbn,
        Integer meetingCnt
) {
}
