package com.booking.booking.stt.repository;

import com.booking.booking.stt.domain.Transcription;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface TranscriptionRepository extends ReactiveMongoRepository<Transcription,String> {

    Mono<Transcription> findByFileName(String filename);

    Mono<Transcription> findByMeetingInfoId(long meetingInfoId);
}
