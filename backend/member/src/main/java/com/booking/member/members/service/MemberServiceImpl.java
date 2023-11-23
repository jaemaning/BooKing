package com.booking.member.members.service;

import com.booking.member.Auth.TokenDto;
import com.booking.member.Auth.TokenProvider;
import com.booking.member.members.domain.Gender;
import com.booking.member.members.domain.Member;
import com.booking.member.members.domain.UserRole;
import com.booking.member.members.dto.ChangeLocationRequestDto;
import com.booking.member.members.dto.MemberInfoResponseDto;
import com.booking.member.members.dto.ModifyRequestDto;
import com.booking.member.members.dto.SignUpRequestDto;
import com.booking.member.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Override
    @Transactional
    public Mono<String> signup(SignUpRequestDto req) {

        return checkMemberDuplicate(req.loginId())
                .flatMap(memberExists -> {
                    if (memberExists) {
                        return Mono.error(new RuntimeException("이미 가입된 회원입니다."));
                    } else {
                        return checkNicknameDuplicate(req.nickname())
                                .flatMap(nicknameExists -> {
                                    if (nicknameExists) {
                                        return Mono.error(new RuntimeException("중복된 닉네임"));
                                    } else {
                                        String[] split = parseAddr(req.address());
                                        Double lat = Double.parseDouble(split[0].trim());
                                        Double lgt = Double.parseDouble(split[1].trim());

                                        Member mem = Member.builder()
                                                .age(req.age())
                                                .email(req.email())
                                                .gender(Gender.valueOf(req.gender()))
                                                .loginId(req.loginId())
                                                .nickname(req.nickname())
                                                .fullName(req.fullName())
                                                .lat(lat)
                                                .lgt(lgt)
                                                .role(UserRole.USER)
                                                .profileImage(req.profileImage())
                                                .provider(req.provider())
                                                .point(0)
                                                .build();

                                        return memberRepository.save(mem)
                                                .then(tokenProvider.createToken(mem.getLoginId(), mem.getId()))
                                                .map(TokenDto::getAccessToken);
                                    }
                                });
                    }
                })
                .onErrorResume(e -> {
                    log.error("회원 가입 에러: {}", e.getMessage());
                    return Mono.error(e);
                });
    }

    @Override
    public Mono<MemberInfoResponseDto> loadMemberInfo(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("사용자를 찾을 수 없습니다.")))
                .flatMap(member-> Mono.just(MemberInfoResponseDto.of(member)));
    }

    @Override
    public Mono<MemberInfoResponseDto> loadMemberInfoByPk(Integer memberPk) {
        return memberRepository.findById(memberPk)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("사용자를 찾을 수 없습니다.")))
                .flatMap(member -> Mono.just(MemberInfoResponseDto.of(member)));
    }

    @Override
    public Mono<MemberInfoResponseDto> loadMemberInfoByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("사용자를 찾을 수 없습니다.")))
                .flatMap(member -> Mono.just(MemberInfoResponseDto.of(member)));
    }

    @Override
    @Transactional
    public Mono<Void> modifyMemberInfo(ModifyRequestDto req) {
        return memberRepository.findByLoginId(req.loginId())
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("사용자를 찾을 수 없습니다.")))
                .flatMap(member -> {
                    if (req.nickname().isEmpty()) {
                        return Mono.error(new RuntimeException("닉네임 빈 문자열"));
                    }

                    member.setNickname(req.nickname());
                    member.setProfileImage(req.profileImage());

                    return memberRepository.save(member);
                })
                .then()
                .onErrorResume(e -> {
                    log.error("회원 수정 에러: {}", e.getMessage());
                    return Mono.error(e);
                });
    }

    @Override
    @Transactional
    public Mono<Void> deleteMember(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("사용자를 찾을 수 없습니다.")))
                .flatMap(memberRepository::delete)
                .then()
                .onErrorResume(e->{
                    log.error("회원 탈퇴 에러: {}", e.getMessage());
                    return Mono.error(e);
                });
    }

    @Override
    public Mono<String> login(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("회원 가입이 필요합니다.")))
                .flatMap(member -> tokenProvider.createToken(loginId, member.getId()))
                .map(TokenDto::getAccessToken)
                .onErrorResume(e -> {
                    log.error("로그인 에러: {}", e.getMessage());
                    return Mono.error(e);
                });
    }

    @Override
    public Mono<Void> changeLocation(ChangeLocationRequestDto req, String loginId) {
        return memberRepository.findByLoginId(loginId)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("사용자를 찾을 수 없습니다.")))
                .flatMap(member -> {
                    String[] split = parseAddr(req.address());
                    Double lat = Double.parseDouble(split[0].trim());
                    Double lgt = Double.parseDouble(split[1].trim());

                    member.setLat(lat);
                    member.setLgt(lgt);
                    return memberRepository.save(member);
                })
                .then()
                .onErrorResume(e->{
                    log.error("회원 위치 수정 에러: {}",e.getMessage());
                    return Mono.error(e);
                });

    }

    public Mono<Boolean> checkMemberDuplicate(String loginId) {
        return memberRepository.existsByLoginId(loginId)
                .defaultIfEmpty(false);
    }

    public Mono<Boolean> checkNicknameDuplicate(String nickname) {
        return memberRepository.existsByNickname(nickname)
                .defaultIfEmpty(false);
    }

    public String[] parseAddr(String address) {
        String addr = address.substring(14);
        addr = addr.substring(0, addr.indexOf("hAcc")).trim();
        return addr.split(",");
    }
}
