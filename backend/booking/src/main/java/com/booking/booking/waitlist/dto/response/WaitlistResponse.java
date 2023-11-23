package com.booking.booking.waitlist.dto.response;

import com.booking.booking.global.dto.response.MemberResponse;

public record WaitlistResponse(
        Integer memberPk,
        String loginId,
        String nickname,
        String profileImage
) {
    public WaitlistResponse(MemberResponse member) {
        this(member.memberPk(), member.loginId(), member.nickname(), member.profileImage());
    }
}
