package com.booking.booking.hashtag.service;

import com.booking.booking.hashtag.domain.Hashtag;
import com.booking.booking.hashtag.dto.response.HashtagResponse;
import com.booking.booking.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public Mono<Hashtag> findByContent(String content) {
        log.info("[Booking:Hashtag] findByContent({})", content);

        return hashtagRepository.findByContent(content);
    }

    public Mono<Hashtag> saveHashtag(String content) {
        log.info("[Booking:Hashtag] saveHashtag({})", content);

        return hashtagRepository.save(Hashtag.builder().content(content).build())
                .onErrorResume(error -> Mono.error(new RuntimeException("해시태그 저장 실패")));
    }

    public Flux<HashtagResponse> findHashtagsByMeetingId(Long meetingId) {
        log.info("[Booking:Hashtag] findHashtagsByMeetingId({})", meetingId);

        return hashtagRepository.findHashtagsByMeetingId(meetingId)
                .flatMapSequential(hashtag -> Mono.just(new HashtagResponse(hashtag)))
                .onErrorResume(error -> Mono.error(new RuntimeException("해시태그 목록 조회 실패")));
    }
}
