package com.booking.booking.global.dto.response;

public record MemberResponse(
    Integer memberPk,
    String loginId,
    String nickname,
    Double lat,
    Double lgt,
    String profileImage
) {
}
