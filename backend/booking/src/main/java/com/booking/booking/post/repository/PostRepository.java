package com.booking.booking.post.repository;

import com.booking.booking.post.domain.Post;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostRepository extends R2dbcRepository<Post, Long> {
    Flux<Post> findAllByMeetingIdOrderByCreatedAtDesc(Long meetingId);
    Mono<Post> findByPostId(Long postId);
    Mono<Void> deleteByPostId(Long postId);
    Mono<Void> deleteAllByMeetingId(Long meetingId);
}
