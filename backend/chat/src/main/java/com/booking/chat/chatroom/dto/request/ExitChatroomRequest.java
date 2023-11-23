package com.booking.chat.chatroom.dto.request;

public record ExitChatroomRequest(
    Long meetingId,
    Long memberId
) {

}
