package com.booking.chat.chatroom.dto.response;

import com.booking.chat.chatroom.domain.Chatroom;
import java.util.List;

public record ChatroomListResponse(
    Long chatroomId,
    Long lastMessageIdx,
    String meetingTitle,
    String lastMessage,
    String coverImage,
    List<Long> memberList

) {
    public static ChatroomListResponse from(Chatroom chatroom) {
        return new ChatroomListResponse(chatroom.get_id(), chatroom.getMessageIndex(), chatroom.getMeetingTitle(), chatroom.getLastMessage(),
            chatroom.getCoverImage(), chatroom.getMemberList());
    }
}
