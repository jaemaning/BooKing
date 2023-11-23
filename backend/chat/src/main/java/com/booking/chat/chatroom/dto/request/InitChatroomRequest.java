package com.booking.chat.chatroom.dto.request;

public record InitChatroomRequest (
    Long meetingId,
    Long leaderId,
    String meetingTitle,
    String coverImage
) {

}
