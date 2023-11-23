package com.booking.booking.stt.service;

import com.booking.booking.stt.dto.request.SttRequestDto;
import com.booking.booking.stt.dto.request.SummaryControllerDto;
import com.booking.booking.stt.dto.request.TranscriptionModificationRequest;
import com.booking.booking.stt.dto.response.LoadSummaryResponse;
import com.booking.booking.stt.dto.response.SttResponseDto;
import com.booking.booking.stt.dto.response.CreateSummaryResponse;
import com.booking.booking.stt.dto.response.TranscriptionResponse;
import reactor.core.publisher.Mono;

public interface SttService {
    Mono<SttResponseDto> speechToText(SttRequestDto requestDto);

    Mono<TranscriptionResponse> findTranscriptionByMeetingInfoId(long meetingInfoId);

    Mono<CreateSummaryResponse> summary(SummaryControllerDto req);

    Mono<LoadSummaryResponse> findFirstByTranscriptionId(String transcriptionId);

    Mono<String> modifyTranscription(TranscriptionModificationRequest request);
}
