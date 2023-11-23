package com.booking.booking.stt.repository;

import com.booking.booking.stt.domain.Summary;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface SummaryRepository extends ReactiveMongoRepository<Summary,String> {

    Mono<Summary> findFirstByTranscriptionIdOrderByCreatedAtDesc(String transcriptionId);
}
