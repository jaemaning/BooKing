package com.booking.booking.hashtagmeeting.service;

import com.booking.booking.hashtag.domain.Hashtag;
import com.booking.booking.hashtag.service.HashtagService;
import com.booking.booking.hashtagmeeting.domain.HashtagMeeting;
import com.booking.booking.hashtagmeeting.repository.HashtagMeetingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class HashtagMeetingService {
    private final HashtagMeetingRepository hashtagMeetingRepository;
    private final HashtagService hashtagService;

    public Mono<Void> saveHashtags(Long meetingId, List<String> hashtagList) {
        log.info("[Booking:HashtagMeeting] saveHashtags({}, {})", meetingId, hashtagList);

        return Flux.fromIterable(hashtagList)
                .flatMapSequential(content ->
                        hashtagService.findByContent(content).switchIfEmpty(hashtagService.saveHashtag(content)))
                .flatMapSequential(hashtag -> mapHashtagToMeeting(meetingId, hashtag))
                .then();
    }

    private Mono<Void> mapHashtagToMeeting(Long meetingId, Hashtag hashtag) {
        log.info("[Booking:HashtagMeeting] mapHashtagToMeeting({}, {})", meetingId, hashtag.getContent());

        return hashtagMeetingRepository
                .save(HashtagMeeting.builder().meetingId(meetingId).hashtagId(hashtag.getHashtagId()).build())
                .onErrorResume(error -> Mono.error(new RuntimeException("해시태그 미팅 연결 실패")))
                .then();
    }

    public Mono<Void> updateHashtags(Long meetingId, List<String> hashtagList) {
        log.info("[Booking:HashtagMeeting] updateHashtags({}, {})", meetingId, hashtagList);

        return this.deleteAllByMeetingId(meetingId)
                .then(this.saveHashtags(meetingId, hashtagList))
                .onErrorResume(Mono::error);
    }

    public Mono<Void> deleteAllByMeetingId(Long meetingId) {
        log.info("[Booking:HashtagMeeting] deleteAllByMeetingId({})", meetingId);

        return hashtagMeetingRepository.deleteAllByMeetingId(meetingId)
                .onErrorResume(error -> Mono.error(new RuntimeException("해시태그 삭제 실패")));
    }
}
