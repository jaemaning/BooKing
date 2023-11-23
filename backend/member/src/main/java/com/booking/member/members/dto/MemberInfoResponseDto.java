package com.booking.member.members.dto;

import com.booking.member.members.domain.Member;

public record MemberInfoResponseDto(
        String loginId,
        String email,
        Integer age,
        String gender,
        String nickname,
        String fullname,
        Double lat,
        Double lgt,
        String profileImage,
        String provider,
        Integer memberPk,
        Integer point
) {
    public static MemberInfoResponseDto of(Member member){
        MemberInfoResponseDto memberInfoResponseDto = new MemberInfoResponseDto(
                member.getLoginId(),
                member.getEmail() == null ? "" : member.getEmail(),
                member.getAge() == null ? -1 : member.getAge(),
                member.getGender() == null ? "" : member.getGender().name(),
                member.getNickname(),
                member.getFullName() == null ? "" : member.getFullName(),
                member.getLat() == null ? -1 : member.getLat(),
                member.getLgt() == null ? -1 : member.getLgt(),
                member.getProfileImage(),
                member.getProvider(),
                member.getId(),
                member.getPoint()
        );
        return memberInfoResponseDto;
    }
}
