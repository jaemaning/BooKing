package com.booking.member.members.dto;

public record SignUpRequestDto(
        String loginId,
        String email,
        Integer age,
        String gender,
        String nickname,
        String fullName,
        String address,
        String profileImage,
        String provider) {
}
