package com.booking.booking.participant.dto.response;

import com.booking.booking.global.dto.response.MemberResponse;
import com.booking.booking.participantstate.domain.ParticipantState;

public record ParticipantResponse(
        Integer memberPk,
        String loginId,
        String nickname,
        String profileImage,
        Boolean attendanceStatus,
        Boolean paymentStatus
) {
    public ParticipantResponse(MemberResponse member) {
        this(member.memberPk(), member.loginId(), member.nickname(), member.profileImage(),
                null, null);
    }

    public ParticipantResponse(MemberResponse member, ParticipantState participantState) {
        this(member.memberPk(), member.loginId(), member.nickname(), member.profileImage(),
                participantState.getAttendanceStatus(), participantState.getPaymentStatus());
    }
}
