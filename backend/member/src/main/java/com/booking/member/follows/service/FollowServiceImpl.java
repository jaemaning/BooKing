package com.booking.member.follows.service;

import com.booking.member.follows.Repository.FollowRepository;
import com.booking.member.follows.domain.Follow;
import com.booking.member.follows.dto.FollowersResponseDto;
import com.booking.member.follows.dto.FollowersResponseDto.Follower;
import com.booking.member.follows.dto.FollowingsResponseDto;
import com.booking.member.follows.dto.FollowingsResponseDto.Following;
import com.booking.member.members.domain.Member;
import com.booking.member.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowServiceImpl implements FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @Override
    public Mono<Void> follow(String loginId, Integer targetMemberPk) {
        return Mono.zip(
                        memberRepository.findByLoginId(loginId),
                        memberRepository.findById(targetMemberPk)
                )
                .flatMap(tuple -> {
                    Member member = tuple.getT1();
                    Member target = tuple.getT2();

                    return Mono.justOrEmpty(member)
                            .switchIfEmpty(Mono.error(new UsernameNotFoundException("사용자를 찾을 수 없습니다.")))
                            .then(Mono.justOrEmpty(target)
                                    .switchIfEmpty(Mono.error(new UsernameNotFoundException("팔로우 대상을 찾을 수 없습니다.")))
                                    .then(followRepository.existsByFollowerAndFollowing(member.getId(), target.getId()))
                                    .flatMap(existingFollow -> {
                                        if (existingFollow) {
                                            log.error("이미 팔로우 상태입니다.");
                                            return Mono.error(new RuntimeException("이미 팔로우 상태입니다."));
                                        } else {
                                            Follow follow = Follow.builder()
                                                    .follower(member.getId())
                                                    .following(target.getId())
                                                    .build();

                                            return followRepository.save(follow);
                                        }
                                    })
                            );
                })
                .then()
                .onErrorResume(e -> {
                    log.error("팔로우 에러: {}", e.getMessage());
                    return Mono.error(e);
                });
    }

    @Override
    public Mono<Void> unfollow(String loginId, Integer targetMemberPk) {
        return Mono.zip(memberRepository.findByLoginId(loginId),
                        memberRepository.findById(targetMemberPk))
                .flatMap(tuple -> {
                    Member member = tuple.getT1();
                    Member target = tuple.getT2();
                    return Mono.justOrEmpty(member)
                            .switchIfEmpty(Mono.error(new UsernameNotFoundException("사용자를 찾을 수 없습니다.")))
                            .then(Mono.justOrEmpty(target)
                                    .switchIfEmpty(Mono.error(new UsernameNotFoundException("언팔로우 대상을 찾을 수 없습니다.")))
                                    .then(followRepository.findByFollowerAndFollowing(member.getId(), targetMemberPk))
                                    .switchIfEmpty(Mono.error(new RuntimeException("팔로우 상태가 아닙니다.")))
                                    .flatMap(followRepository::delete)
                                    .then());
                })
                .onErrorResume(e -> {
                    log.error("언팔로우 에러: {}", e.getMessage());
                    return Mono.error(e);
                });
    }

    @Override
    public Mono<FollowersResponseDto> getFollowers(Integer memberPk) {
        return memberRepository.findById(memberPk)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("사용자를 찾을 수 없습니다.")))
                .flatMapMany(member -> followRepository.findByFollowing(member.getId()))
                .flatMap(follow -> memberRepository.findById(follow.getFollower())
                        .map(followerMember -> new Follower(
                                followerMember.getId(),
                                followerMember.getNickname(),
                                followerMember.getProfileImage()
                        )))
                .collectList()
                .map(followerDetails -> FollowersResponseDto.builder()
                        .followers(followerDetails)
                        .followersCnt(followerDetails.size())
                        .build())
                .switchIfEmpty(Mono.just(FollowersResponseDto.builder()
                        .followers(new ArrayList<>())
                        .followersCnt(0)
                        .build()))
                .onErrorResume(e -> {
                    log.error("팔로워 조회 에러: {}", e.getMessage());
                    return Mono.error(e);
                });
    }

    @Override
    public Mono<FollowingsResponseDto> getFollowings(Integer memberPk) {
        return memberRepository.findById(memberPk)
                .flatMapMany(member -> followRepository.findByFollower(member.getId()))
                .flatMap(follow -> memberRepository.findById(follow.getFollowing())
                        .map(followingMember -> new Following(
                                followingMember.getId(),
                                followingMember.getNickname(),
                                followingMember.getProfileImage()
                        )))
                .collectList()
                .map(followingDetails -> FollowingsResponseDto.builder()
                        .followings(followingDetails)
                        .followingsCnt(followingDetails.size())
                        .build())
                .switchIfEmpty(Mono.just(FollowingsResponseDto.builder()
                        .followings(new ArrayList<>())
                        .followingsCnt(0)
                        .build()))
                .onErrorResume(e -> {
                    log.error("팔로잉 조회 에러: {}", e.getMessage());
                    return Mono.error(e);
                });
    }
}
