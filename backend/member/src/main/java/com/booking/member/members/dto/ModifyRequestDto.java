package com.booking.member.members.dto;

public record ModifyRequestDto(
        String loginId,
        String nickname,
        String profileImage
) {
}
