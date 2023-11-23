package com.booking.chat.chatroom.dto.request;

public record JoinChatroomRequest(
    Long meetingId,
    Long memberId
) {

}
