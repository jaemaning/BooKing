package com.booking.booking.participantstate.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "participants_state")
public class ParticipantState {
    @Id
    private long stateId;

    private Integer memberId;

    private Long meetinginfoId;

    private Boolean attendanceStatus;

    private Boolean paymentStatus;

    public ParticipantState updateAttendance(Boolean attendance) {
        return ParticipantState.builder()
                .stateId(stateId)
                .memberId(memberId)
                .meetinginfoId(meetinginfoId)
                .attendanceStatus(attendance)
                .paymentStatus(paymentStatus)
                .build();
    }

    public ParticipantState updatePayment(Boolean payment) {
        return ParticipantState.builder()
                .stateId(stateId)
                .memberId(memberId)
                .meetinginfoId(meetinginfoId)
                .attendanceStatus(attendanceStatus)
                .paymentStatus(payment)
                .build();
    }
}
