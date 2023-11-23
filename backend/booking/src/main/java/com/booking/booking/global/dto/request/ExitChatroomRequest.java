package com.booking.booking.global.dto.request;

public record ExitChatroomRequest(
        Long meetingId,
        Integer memberId
) {
}
