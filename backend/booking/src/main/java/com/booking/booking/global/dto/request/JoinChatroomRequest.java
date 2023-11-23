package com.booking.booking.global.dto.request;

public record JoinChatroomRequest(
        Long meetingId,
        Integer memberId
) {
}
