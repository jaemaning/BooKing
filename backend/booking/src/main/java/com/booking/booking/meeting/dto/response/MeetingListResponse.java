package com.booking.booking.meeting.dto.response;

import com.booking.booking.global.dto.response.BookResponse;
import com.booking.booking.hashtag.dto.response.HashtagResponse;
import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.meeting.domain.MeetingState;

import java.util.List;

public record MeetingListResponse(
        Long meetingId,
        String bookIsbn,
        String bookTitle,
        String coverImage,
        String meetingTitle,
        Integer curParticipants,
        Integer maxParticipants,
        MeetingState meetingState,
        Double lat,
        Double lgt,
        List<HashtagResponse> hashtagList,
        String address
) {
    public MeetingListResponse(Meeting meeting, BookResponse book, Integer curParticipants,
                               List<HashtagResponse> hashtagList) {
        this(meeting.getMeetingId(), book.isbn(), book.title(), book.coverImage(),
                meeting.getMeetingTitle(), curParticipants, meeting.getMaxParticipants(), meeting.getMeetingState(),
                meeting.getLat(), meeting.getLgt(), hashtagList, meeting.getAddress());
    }
}
