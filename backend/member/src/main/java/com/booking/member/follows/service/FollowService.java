package com.booking.member.follows.service;

import com.booking.member.follows.dto.FollowersResponseDto;
import com.booking.member.follows.dto.FollowingsResponseDto;
import reactor.core.publisher.Mono;

public interface FollowService {
    Mono<Void> follow(String loginId,Integer targetMemberPk);

    Mono<Void> unfollow(String loginId,Integer targetMemberPk);

    Mono<FollowersResponseDto> getFollowers(Integer memberPk);

    Mono<FollowingsResponseDto> getFollowings(Integer memberPk);
}
