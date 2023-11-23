package com.booking.booking.participantstate.service;

import com.booking.booking.meetinginfo.domain.MeetingInfo;
import com.booking.booking.participant.domain.Participant;
import com.booking.booking.participantstate.domain.ParticipantState;
import com.booking.booking.participantstate.repository.ParticipantStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParticipantStateService {
    private final ParticipantStateRepository participantStateRepository;

    public Flux<ParticipantState> findParticipantStatesByMeetingId(Long meetingId) {
        log.info("[Booking:ParticipantState] findParticipantStatesByMeetingId({})", meetingId);

        return participantStateRepository.findParticipantStatesByMeetingId(meetingId);
    }

    public Mono<ParticipantState> findByMeetingIdAndMemberId(Long meetingId, Integer memberId) {
        log.info("[Booking:ParticipantState] findByMeetingIdAndMemberId({}, {})", meetingId, memberId);

        return participantStateRepository.findByMeetinginfoIdAndMemberId(meetingId, memberId);
    }

    public Mono<Void> startMeeting(MeetingInfo meetingInfo, Participant participant) {
        log.info("[Booking:ParticipantState] startMeeting({}, {})", meetingInfo, participant);

        return participantStateRepository.save(ParticipantState.builder()
                        .memberId(participant.getMemberId())
                        .meetinginfoId(meetingInfo.getMeetinginfoId())
                        .attendanceStatus(false)
                        .paymentStatus(meetingInfo.getFee() == 0)
                        .build())
                .then();
    }

    public Mono<Void> attendMeeting(ParticipantState participantState) {
        log.info("[Booking:ParticipantState] attendMeeting({})", participantState);

        return participantStateRepository.save(participantState.updateAttendance(true))
                .then();
    }

    public Mono<Void> payMeeting(ParticipantState participantState) {
        log.info("[Booking:ParticipantState] payMeeting({})", participantState);

        return participantStateRepository.save(participantState.updatePayment(true))
                .then();
    }
}
