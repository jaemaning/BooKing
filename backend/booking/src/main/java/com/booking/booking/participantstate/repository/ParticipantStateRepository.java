package com.booking.booking.participantstate.repository;

import com.booking.booking.participantstate.domain.ParticipantState;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ParticipantStateRepository extends R2dbcRepository<ParticipantState, Long> {
    @Query("SELECT ps.* " +
            "FROM participants_state ps " +
            "WHERE ps.meetinginfo_id = " +
            "        (SELECT MAX(meetinginfo_id) FROM meetinginfos WHERE meeting_id = :meetingId)")
    Flux<ParticipantState> findParticipantStatesByMeetingId(@Param("meetingId") Long meetingId);

    Mono<ParticipantState> findByMeetinginfoIdAndMemberId(Long meetinginfoId, Integer memberId);
}
