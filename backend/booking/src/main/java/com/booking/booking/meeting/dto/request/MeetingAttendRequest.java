package com.booking.booking.meeting.dto.request;

public record MeetingAttendRequest(
        Long meetingId,
        Double lat,
        Double lgt
) {
}
