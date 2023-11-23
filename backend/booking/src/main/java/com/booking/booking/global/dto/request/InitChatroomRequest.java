package com.booking.booking.global.dto.request;

import com.booking.booking.global.dto.response.BookResponse;
import com.booking.booking.meeting.domain.Meeting;

public record InitChatroomRequest(
        Long meetingId,
        Integer leaderId,
        String meetingTitle,
        String coverImage
) {
    public InitChatroomRequest(Meeting meeting, BookResponse book) {
        this(meeting.getMeetingId(), meeting.getLeaderId(), meeting.getMeetingTitle(), book.coverImage());
    }
}
