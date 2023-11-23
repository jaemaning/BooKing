package com.booking.booking.stt.dto;

import lombok.Data;

@Data
public class MessageDto {
    private String role;
    private String content;

    public MessageDto(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
