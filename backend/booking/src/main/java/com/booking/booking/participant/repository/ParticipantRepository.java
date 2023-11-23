package com.booking.booking.participant.repository;

import com.booking.booking.participant.domain.Participant;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ParticipantRepository extends R2dbcRepository<Participant, Long> {
    Mono<Integer> countAllByMeetingId(Long meetingId);
    Flux<Participant> findAllByMeetingId(Long meetingId);
    Mono<Boolean> existsByMeetingIdAndMemberId(Long meetingId, Integer memberId);
    Mono<Void> deleteAllByMeetingId(Long meetingId);
    Mono<Void> deleteByMeetingIdAndMemberId(Long meetingId, Integer memberId);
}
