package com.booking.member.members.repository;

import com.booking.member.members.domain.Member;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface MemberRepository extends R2dbcRepository<Member,Integer> {
    Mono<Member> findByEmailAndProvider(String email, String provider);

    Mono<Member> findByEmail(String email);
    Mono<Member> findByLoginId(String loginId);
    Mono<Member> findByNickname(String nickname);

//    boolean existsByEmail(String email);
    Mono<Boolean> existsByLoginId(String loginId);

    Mono<Boolean> existsByNickname(String nickname);
}
