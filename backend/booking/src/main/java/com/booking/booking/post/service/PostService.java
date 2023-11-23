package com.booking.booking.post.service;

import com.booking.booking.global.utils.MemberUtil;
import com.booking.booking.post.domain.Post;
import com.booking.booking.post.dto.response.PostListResponse;
import com.booking.booking.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final MemberUtil memberUtil;

    public Mono<Post> createPost(Post post) {
        log.info("[Booking:Post] createPost({})", post);

        return postRepository.save(post)
                .onErrorResume(error -> Mono.error(new RuntimeException("게시글 저장 실패")));
    }

    public Flux<PostListResponse> findAllByMeetingId(Long meetingId) {
        log.info("[Booking:Post] findAllByMeetingId({})", meetingId);

        return postRepository.findAllByMeetingIdOrderByCreatedAtDesc(meetingId)
                .flatMapSequential(post -> memberUtil.getMemberInfoByPk(post.getMemberId())
                        .flatMap(member -> Mono.just(new PostListResponse(post, member))))
                .onErrorResume(error -> Mono.error(new RuntimeException("게시글 목록 조회 실패")));
    }

    public Mono<Post> findByPostId(Long postId) {
        log.info("[Booking:Post] findByPostId({})", postId);

        return postRepository.findByPostId(postId)
                .onErrorResume(error -> Mono.error(new RuntimeException("게시글 상세 조회 실패")));
    }
    
    public Mono<Post> updatePost(Post post) {
        log.info("[Booking:Post] updatePost({})", post);

        return postRepository.save(post)
                .onErrorResume(error -> Mono.error(new RuntimeException("게시글 수정 실패")));
    }
    
    public Mono<Void> deleteByPostId(Long postId) {
        log.info("[Booking:Post] deleteByPostId({})", postId);

        return postRepository.deleteByPostId(postId)
                .onErrorResume(error -> Mono.error(new RuntimeException("게시글 삭제 실패")));
    }

    public Mono<Void> deleteAllByMeetingId(Long meetingId) {
        log.info("[Booking:Post] deleteAllByMeetingId({})", meetingId);

        return postRepository.deleteAllByMeetingId(meetingId)
                .onErrorResume(error -> Mono.error(new RuntimeException("게시글 삭제 실패")));
    }
}
