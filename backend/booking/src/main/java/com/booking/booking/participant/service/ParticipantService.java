package com.booking.booking.participant.service;

import com.booking.booking.global.utils.MemberUtil;
import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.participant.domain.Participant;
import com.booking.booking.participant.dto.response.ParticipantResponse;
import com.booking.booking.participant.repository.ParticipantRepository;
import com.booking.booking.participantstate.service.ParticipantStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final ParticipantStateService participantStateService;
    private final MemberUtil memberUtil;

    public Mono<Integer> countAllByMeetingId(Long meetingId) {
        log.info("[Booking:Participant] countAllByMeetingId({})", meetingId);

        return participantRepository.countAllByMeetingId(meetingId);
    }

    public Mono<Void> addParticipant(Meeting meeting, Integer memberId) {
        log.info("[Booking:Participant] addParticipant({}, {})", meeting, memberId);

        return countAllByMeetingId(meeting.getMeetingId())
                .flatMap(count -> {
                    if (count >= meeting.getMaxParticipants()) {
                        return Mono.error(new RuntimeException("모임이 꽉 찼어요"));
                    }
                    return participantRepository.save(
                            Participant.builder()
                                    .memberId(memberId)
                                    .meetingId(meeting.getMeetingId())
                                    .build());
                })
                .then();
    }

    public Flux<ParticipantResponse> findAllResponseByMeetingId(Long meetingId) {
        log.info("[Booking:Participant] findAllResponseByMeetingId({})", meetingId);

        return participantStateService.findParticipantStatesByMeetingId(meetingId)
                .flatMapSequential(participantState -> memberUtil.getMemberInfoByPk(participantState.getMemberId())
                        .flatMap(member -> Mono.just(new ParticipantResponse(member, participantState))))
                .switchIfEmpty(findAllByMeetingId(meetingId) // 진행 중 아닐 때
                        .flatMapSequential(participant -> memberUtil.getMemberInfoByPk(participant.getMemberId()))
                        .flatMapSequential(member -> Mono.just(new ParticipantResponse(member))));
    }

    public Flux<Participant> findAllByMeetingId(Long meetingId) {
        log.info("[Booking:Participant] findAllByMeetingId({})", meetingId);

        return participantRepository.findAllByMeetingId(meetingId)
                .onErrorResume(error -> Flux.error(new RuntimeException("참가자 목록 조회 실패")));
    }

    public Mono<Boolean> existsByMeetingIdAndMemberId(Long meetingId, Integer memberId) {
        log.info("[Booking:Participant] existsByMeetingIdAndMemberId({}, {})", meetingId, memberId);

        return participantRepository.existsByMeetingIdAndMemberId(meetingId, memberId);
    }

    public Mono<Void> deleteByMeetingIdAndMemberId(Long meetingId, Integer memberId) {
        log.info("[Booking:Participant] deleteByMeetingIdAndMemberId({}, {})", meetingId, memberId);

        return participantRepository.deleteByMeetingIdAndMemberId(meetingId, memberId);
    }

    public Mono<Void> deleteAllByMeetingId(Long meetingId) {
        log.info("[Booking:Participant] deleteAllByMeetingId({})", meetingId);

        return participantRepository.deleteAllByMeetingId(meetingId);
    }
}
