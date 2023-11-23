package com.booking.booking.stt.service;

import com.booking.booking.stt.domain.Summary;
import com.booking.booking.stt.domain.Transcription;
import com.booking.booking.stt.dto.request.SttRequestDto;
import com.booking.booking.stt.dto.request.SummaryControllerDto;
import com.booking.booking.stt.dto.request.SummaryRequest;
import com.booking.booking.stt.dto.request.TranscriptionModificationRequest;
import com.booking.booking.stt.dto.response.LoadSummaryResponse;
import com.booking.booking.stt.dto.response.SttResponseDto;
import com.booking.booking.stt.dto.response.CreateSummaryResponse;
import com.booking.booking.stt.dto.response.TranscriptionResponse;
import com.booking.booking.stt.repository.SummaryRepository;
import com.booking.booking.stt.repository.TranscriptionRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SttServiceImpl implements SttService{

    @Value("${stt.invoke-url}")
    private String invokeUrl;
    @Value("${stt.key}")
    private String sttKey;
    @Value("${naver.id}")
    private String naverId;
    @Value("${naver.key}")
    private String naverKey;

    private final TranscriptionRepository transcriptionRepository;
    private final SummaryRepository summaryRepository;
//    private final WebClient sttWebClient= WebClient.create(invokeUrl);
//    private final WebClient naverWebClient= WebClient.create("https://naveropenapi.apigw.ntruss.com");
    @Override
    public Mono<SttResponseDto> speechToText(SttRequestDto requestDto) {
        WebClient sttWebClient= WebClient.create(invokeUrl);
        Map<String, Object> requestBody = new HashMap<>();
        //음성 001.m4a
        requestBody.put("dataKey","recording/"+requestDto.fileName());
        requestBody.put("language","ko-KR");
        requestBody.put("completion","sync");
        requestBody.put("fullText",Boolean.TRUE);
        requestBody.put("resultToObs",Boolean.TRUE);
        requestBody.put("diarization.enable",Boolean.TRUE);

        return sttWebClient.post()
                .uri("/recognizer/object-storage")
                .header("Content-Type","application/json")
                .header("X-CLOVASPEECH-API-KEY",sttKey)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class).doOnNext(body -> {
                        log.error("4xx error: {}", body);
                    }).then(Mono.error(new RuntimeException("Client error")));
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class).doOnNext(body -> {
                        log.error("5xx error: {}", body);
                    }).then(Mono.error(new RuntimeException("Server error")));
                })
                .bodyToMono(SttResponseDto.class)
                .flatMap(sttResponseDto -> saveTranscription(sttResponseDto,requestDto.fileName(), requestDto.meetingInfoId()).thenReturn(sttResponseDto))
                .doOnNext(resp-> log.info("stt 결과 {}",resp));
    }

    @Override
    public Mono<TranscriptionResponse> findTranscriptionByMeetingInfoId(long meetingInfoId) {
        return transcriptionRepository.findByMeetingInfoId(meetingInfoId)
                .flatMap(transcription -> Mono.just(new TranscriptionResponse(transcription)))
                .switchIfEmpty(Mono.error(new RuntimeException("Not found Transcription")));

    }

    public Mono<CreateSummaryResponse> summary(SummaryControllerDto req) {
        WebClient naverWebClient= WebClient.create("https://naveropenapi.apigw.ntruss.com");
        SummaryRequest request=new SummaryRequest(req.getContent());
        return naverWebClient.post()
                .uri("/text-summary/v1/summarize")
                .header("X-NCP-APIGW-API-KEY-ID",naverId)
                .header("X-NCP-APIGW-API-KEY",naverKey)
                .header("Content-Type","application/json")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class).doOnNext(body -> {
                        // 로그에 오류 본문을 출력
                        log.error("4xx error: {}", body);
                    }).then(Mono.error(new RuntimeException("Client error")));
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class).doOnNext(body -> {
                        // 로그에 오류 본문을 출력
                        log.error("5xx error: {}", body);
                    }).then(Mono.error(new RuntimeException("Server error")));
                })
                .bodyToMono(CreateSummaryResponse.class)
                .flatMap(createSummaryResponse -> saveSummary(createSummaryResponse,req).thenReturn(createSummaryResponse))
                .doOnNext(resp-> log.info("summary result : {}",resp));
    }

    public Mono<LoadSummaryResponse> findFirstByTranscriptionId(String transcriptionId) {
        return summaryRepository.findFirstByTranscriptionIdOrderByCreatedAtDesc(transcriptionId)
                .flatMap(summary -> Mono.just(new LoadSummaryResponse(summary)));
    }

    @Override
    public Mono<String> modifyTranscription(TranscriptionModificationRequest request) {
        return transcriptionRepository.findById(request.getId())
                .flatMap(transcription -> {
                    transcription.setExcludeId(request);
                    return transcriptionRepository.save(transcription);
                })
                .switchIfEmpty(Mono.error(new NotFoundException("Not found transcription")))
                .thenReturn("transcription modification");
    }

    private Mono<Transcription> saveTranscription(SttResponseDto sttResponseDto,String fileName,long meetingInfoId) {
        Transcription transcription = Transcription.of(sttResponseDto,fileName,meetingInfoId);
        return transcriptionRepository.save(transcription);
    }

    private Mono<Summary> saveSummary(CreateSummaryResponse createSummaryResponse,SummaryControllerDto summaryControllerDto) {
        Summary summary= Summary.of(createSummaryResponse.getSummary(),summaryControllerDto.getTranscriptionId());
        return summaryRepository.save(summary);
    }
}
