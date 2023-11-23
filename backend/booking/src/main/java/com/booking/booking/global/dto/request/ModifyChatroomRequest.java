package com.booking.booking.global.dto.request;

public record ModifyChatroomRequest(
        Long meetingId,
        String meetingTitle
) {
}
