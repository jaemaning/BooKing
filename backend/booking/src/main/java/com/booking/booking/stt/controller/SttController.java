package com.booking.booking.stt.controller;

import com.booking.booking.stt.dto.request.SttRequestDto;
import com.booking.booking.stt.dto.request.TranscriptionModificationRequest;
import com.booking.booking.stt.dto.response.LoadSummaryResponse;
import com.booking.booking.stt.dto.response.SttResponseDto;
import com.booking.booking.stt.dto.request.SummaryControllerDto;
import com.booking.booking.stt.dto.response.CreateSummaryResponse;
import com.booking.booking.stt.dto.response.TranscriptionResponse;
import com.booking.booking.stt.service.SttService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/booking")
@Slf4j
public class SttController {

    private final SttService sttService;
    private final String AUTH="Authorization";

    @PostMapping("/stt")
    public Mono<ResponseEntity<SttResponseDto>> speechToText(@RequestHeader(AUTH) String token,
                                                             @RequestBody SttRequestDto requestDto) {
        log.info("stt 요청 파일 이름 {}",requestDto.fileName());
        return sttService.speechToText(requestDto)
                .map(resp->ResponseEntity.ok().body(resp));
    }
    @PostMapping("/summary")
    public Mono<ResponseEntity<CreateSummaryResponse>> summary(@RequestHeader(AUTH) String token,
                                                               @RequestBody SummaryControllerDto reqDto) {
        log.info("요약 요청 {}",reqDto.getContent());
        return sttService.summary(reqDto)
                .map(resp->ResponseEntity.ok().body(resp));
    }

    @GetMapping("/stt")
    public Mono<ResponseEntity<TranscriptionResponse>> loadTranscriptionByMeetingInfoId(@RequestParam("meetingInfoId") long meetingInfoId){
        log.info("Load Transcription meetingInfoId: {}",meetingInfoId);
        return sttService.findTranscriptionByMeetingInfoId(meetingInfoId)
                .flatMap(resp->Mono.just(ResponseEntity.ok().body(resp)))
                .onErrorResume(e->{
                    log.error("Load Transcription Error: {}",e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    @GetMapping("/summary")
    public Mono<ResponseEntity<LoadSummaryResponse>> loadLatestSummaryByTranscriptionId(@RequestParam("transcriptionId") String transcriptionId) {
        log.info("Load latest summary by transcriptionId : {}",transcriptionId);
        return sttService.findFirstByTranscriptionId(transcriptionId)
                .flatMap(resp -> Mono.just(ResponseEntity.ok().body(resp)));
    }

    @PostMapping("/stt/modification")
    public Mono<ResponseEntity<String>> modifyTranscription(@RequestBody TranscriptionModificationRequest request) {
        log.info("Transcription modification : {}",request);
        return sttService.modifyTranscription(request)
                .flatMap(resp->Mono.just(ResponseEntity.ok().body(resp)))
                .onErrorResume(e->{
                    log.error("transcription modification error : {}",e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(e.getMessage()));
                });
    }
}
