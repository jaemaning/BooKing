package com.booking.booking.waitlist.repository;

import com.booking.booking.waitlist.domain.Waitlist;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WaitlistRepository extends R2dbcRepository<Waitlist, Long> {
    Mono<Boolean> existsByMeetingIdAndMemberId(Long meetingId, Integer memberId);
    Mono<Void> deleteByMeetingIdAndMemberId(Long meetingId, Integer memberId);
    Flux<Waitlist> findAllByMeetingId(Long meetingId);
    Mono<Void> deleteAllByMeetingId(Long meetingId);
}
