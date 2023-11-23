package com.booking.member.follows.Repository;

import com.booking.member.follows.domain.Follow;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FollowRepository extends R2dbcRepository<Follow,Integer> {

    Mono<Follow> findByFollowerAndFollowing(Integer follower, Integer following);

    Mono<Boolean> existsByFollowerAndFollowing(Integer follower,Integer following);

    //팔로워들 찾기
    Flux<Follow> findByFollowing(Integer following);

    //팔로잉들 찾기
    Flux<Follow> findByFollower(Integer follower);
}
