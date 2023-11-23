package com.booking.book.memberbook.dto.response;

import com.booking.book.book.domain.Book;
import com.booking.book.book.dto.response.BookResponse;
import com.booking.book.memberbook.domain.MemberBook;
import com.booking.book.memberbook.domain.Note;

import java.time.LocalDateTime;
import java.util.List;

public record MemberBookResponse(
        String id,
        Integer memberPk,
        BookResponse bookInfo,
        List<Note> notes,
        LocalDateTime createdAt
) {
    public MemberBookResponse(MemberBook memberBook, Book book) {
        this(
                memberBook.get_id(),
                memberBook.getMemberPk(),
                new BookResponse(book),
                memberBook.getNotes(),
                memberBook.getCreatedAt()
        );
    }
}
