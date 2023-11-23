package com.booking.booking.hashtag.repository;

import com.booking.booking.hashtag.domain.Hashtag;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HashtagRepository extends R2dbcRepository<Hashtag, Long> {
    Mono<Hashtag> findByContent(String content);

    @Query("SELECT h.* " +
            "FROM hashtags h JOIN hashtag_meeting hm ON h.hashtag_id = hm.hashtag_id " +
            "WHERE hm.meeting_id = :meetingId")
    Flux<Hashtag> findHashtagsByMeetingId(@Param("meetingId") Long meetingId);
}
