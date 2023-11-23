package com.booking.booking.meeting.dto.response;

import com.booking.booking.global.dto.response.BookResponse;
import com.booking.booking.hashtag.dto.response.HashtagResponse;
import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.meeting.domain.MeetingState;
import com.booking.booking.meetinginfo.dto.response.MeetingInfoResponse;

import java.util.List;

public record MeetingDetailResponse(
        Long meetingId,
        Integer leaderId,
        String bookIsbn,
        String bookTitle,
        String bookAuthor,
        String bookContent,
        String coverImage,
        String meetingTitle,
        String description,
        Double lat,
        Double lgt,
        Integer curParticipants,
        Integer maxParticipants,
        MeetingState meetingState,
        List<HashtagResponse> hashtagList,
        List<MeetingInfoResponse> meetingInfoList
) {
    public MeetingDetailResponse(Meeting meeting, BookResponse book, Integer curParticipants,
                                 List<HashtagResponse> hashtagList, List<MeetingInfoResponse> meetingInfoList) {
        this(meeting.getMeetingId(), meeting.getLeaderId(),
                book.isbn(), book.title(), book.author(), book.content(), book.coverImage(),
                meeting.getMeetingTitle(), meeting.getDescription(), meeting.getLat(), meeting.getLgt(),
                curParticipants, meeting.getMaxParticipants(), meeting.getMeetingState(),
                hashtagList, meetingInfoList);
    }
}
