package com.booking.chat.message.dto.response;

import com.booking.chat.message.domain.Message;
import java.time.LocalDateTime;

public record MessageResponse(
    Long chatroomId,
    Long messageId,
    Long senderId,
    String content,
    Integer readCount,
    LocalDateTime timestamp
) {

    public MessageResponse(Message message) {
        this (
            message.getChatroomId(),
            message.getMessageId(),
            message.getMemberId(),
            message.getContent(),
            message.getReadCount(),
            message.getTimestamp()
        );
    }

}
