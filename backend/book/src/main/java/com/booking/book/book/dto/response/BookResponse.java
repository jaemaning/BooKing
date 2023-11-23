package com.booking.book.book.dto.response;

import com.booking.book.book.domain.Book;

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

    public BookResponse(Book book) {
        this(
                book.getTitle(),
                book.getAuthor(),
                book.getCoverImage(),
                book.getGenre(),
                book.getPublishDate(),
                book.getContent(),
                book.getIsbn(),
                book.getMeetingCnt()
        );
    }
}
