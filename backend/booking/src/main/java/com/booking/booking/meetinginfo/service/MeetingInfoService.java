package com.booking.booking.meetinginfo.service;

import com.booking.booking.meetinginfo.domain.MeetingInfo;
import com.booking.booking.meetinginfo.dto.response.MeetingInfoResponse;
import com.booking.booking.meetinginfo.repository.MeetingInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetingInfoService {
    private final MeetingInfoRepository meetingInfoRepository;

    public Mono<MeetingInfo> createMeetingInfo(MeetingInfo meetingInfo) {
        log.info("[Booking:MeetingInfo] createMeetingInfo({})", meetingInfo);
        
        if (meetingInfo.getDate().isBefore(LocalDateTime.now().plusHours(1L))) {
            return Mono.error(new RuntimeException("시간을 다시 입력해주세요"));
        } else if (meetingInfo.getFee() < 0 || meetingInfo.getFee() >= 100000) {
            return Mono.error(new RuntimeException("참가비를 다시 입력해주세요"));
        }
        return meetingInfoRepository.save(meetingInfo)
                .onErrorResume(error -> Mono.error(new RuntimeException("미팅정보 저장 실패")));
    }

    public Flux<MeetingInfoResponse> findAllByMeetingId(Long meetingId) {
        log.info("[Booking:MeetingInfo] findAllByMeetingId({})", meetingId);

        return meetingInfoRepository.findAllByMeetingIdOrderByDateDesc(meetingId)
                .flatMapSequential(meetingInfo -> Mono.just(new MeetingInfoResponse(meetingInfo)))
                .onErrorResume(error -> Mono.error(new RuntimeException("미팅정보 목록 조회 실패")));
    }

    public Mono<MeetingInfo> findByMeetingId(Long meetingId) {
        log.info("[Booking:MeetingInfo] findByMeetingId({})", meetingId);

        return meetingInfoRepository.findLatestByMeetingId(meetingId)
                .onErrorResume(error -> Mono.error(new RuntimeException("미팅정보 목록 조회 실패")));
    }
}
