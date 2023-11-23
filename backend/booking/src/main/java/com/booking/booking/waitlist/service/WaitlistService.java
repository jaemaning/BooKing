package com.booking.booking.waitlist.service;

import com.booking.booking.global.utils.MemberUtil;
import com.booking.booking.waitlist.domain.Waitlist;
import com.booking.booking.waitlist.dto.response.WaitlistResponse;
import com.booking.booking.waitlist.repository.WaitlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class WaitlistService {
    private final WaitlistRepository waitlistRepository;
    private final MemberUtil memberUtil;

    public Mono<Boolean> existsByMeetingIdAndMemberId(Long meetingId, Integer memberId) {
        log.info("[Booking:Waitlist] existsByMeetingIdAndMemberId({}, {})", meetingId, memberId);

        return waitlistRepository.existsByMeetingIdAndMemberId(meetingId, memberId);
    }

    public Mono<Void> enrollMeeting(Long meetingId, Integer memberId) {
        log.info("[Booking:Waitlist] enrollMeeting({}, {})", meetingId, memberId);

        return waitlistRepository.save(Waitlist.builder().meetingId(meetingId).memberId(memberId).build())
                .onErrorResume(error -> Mono.error(new RuntimeException("대기 목록 추가 실패")))
                .then();
    }

    public Mono<Void> deleteByMeetingIdAndMemberId(Long meetingId, Integer memberId) {
        log.info("[Booking:Waitlist] deleteByMeetingIdAndMemberId({}, {})", meetingId, memberId);

        return waitlistRepository.deleteByMeetingIdAndMemberId(meetingId, memberId)
                .onErrorResume(error -> Mono.error(new RuntimeException("대기 목록 삭제 실패")));
    }

    public Flux<WaitlistResponse> findAllByMeetingId(Long meetingId) {
        log.info("[Booking:Waitlist] findAllByMeetingId({})", meetingId);

        return waitlistRepository.findAllByMeetingId(meetingId)
                .flatMapSequential(waitlist -> memberUtil.getMemberInfoByPk(waitlist.getMemberId()))
                .flatMapSequential(member -> Mono.just(new WaitlistResponse(member)))
                .onErrorResume(error -> Flux.error(new RuntimeException("대기자 목록 조회 실패")));
    }

    public Mono<Void> deleteAllByMeetingId(Long meetingId) {
        log.info("[Booking:Waitlist] deleteAllByMeetingId({})", meetingId);

        return waitlistRepository.deleteAllByMeetingId(meetingId)
                .onErrorResume(error -> Mono.error(new RuntimeException("대기자 목록 삭제 실패")));
    }
}
