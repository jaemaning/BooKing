package com.booking.chat.notification.repository;

import com.booking.chat.notification.domain.NotificationInformation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface NotificationInformationRepository extends ReactiveMongoRepository<NotificationInformation, Long> {

    Mono<NotificationInformation> findByMemberId(Long memberId);
}
