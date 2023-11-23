package com.booking.book.memberbook.dto.request;

public record RegisterNoteRequest(
        Integer memberPk,
        String isbn,
        String content
) {
    public String toString() {
        return "{ memberPk: "+this.memberPk+", isbn: "+this.isbn+", content: "+this.content+" }";
    }
}
