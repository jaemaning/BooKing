package com.booking.member.members.controller;

import com.booking.member.members.dto.*;
import com.booking.member.members.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    Mono<ResponseEntity<String>> signup(@RequestBody SignUpRequestDto req) {
        log.info("회원 가입 요청={}", req);
        return memberService.signup(req)
                .map(token -> ResponseEntity.ok().body(token))
                .onErrorResume(e -> {
                    log.error("회원 가입 에러: {}", e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(e.getMessage()));
                });
    }

    @GetMapping("/memberInfo/{loginId}")
    Mono<ResponseEntity<MemberInfoResponseDto>> loadMember(@PathVariable String loginId) {
        log.info("유저 정보 조회 loginId={}", loginId);

        return memberService.loadMemberInfo(loginId)
                .map(response -> ResponseEntity.ok().body(response))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/afterLogin/{token}")
    Mono<ResponseEntity<String>> loadMemberAfterLogin(@PathVariable String token) {
        try {
            return Mono.just(ResponseEntity.ok().body(token));
        } catch (Exception e) {
//            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
            return Mono.error(e);
        }
    }

    @PatchMapping("/modification")
    Mono<ResponseEntity<String>> modifyMemberInfo(@AuthenticationPrincipal UserDetails user,
                                                  @RequestBody ModifyRequestDto req) {
        log.info("회원 정보 수정 요청={}", req);
        return memberService.modifyMemberInfo(req)
                .then(Mono.just(ResponseEntity.ok().body("유저 정보 수정 완료")))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }

    @DeleteMapping("/deletion/{loginId}")
    Mono<ResponseEntity<String>> deleteMember(@PathVariable String loginId) {
        log.info("회원 탈퇴 {}", loginId);
        return memberService.deleteMember(loginId)
                .thenReturn(ResponseEntity.ok().body("탈퇴"))
                .onErrorResume(e->{
                    log.error("탈퇴 에러 {}",e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(e.getMessage()));
                });
    }

    @PostMapping("/login")
    Mono<ResponseEntity<String>> login(@RequestBody LoginRequestDto reqDto) {
        log.info("로그인 요청 id: {}", reqDto.loginId());
        return memberService.login(reqDto.loginId())
                .map(token -> ResponseEntity.ok().body(token))
                .onErrorResume(e -> {
                    log.info("로그인 에러: {}", e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(e.getMessage()));
                });
    }

    @PatchMapping("/location")
    Mono<ResponseEntity<Void>> changeLocation(@AuthenticationPrincipal UserDetails user,
                                              @RequestBody ChangeLocationRequestDto reqDto){
        log.info("위치 수정 요청: {}",reqDto);
        return memberService.changeLocation(reqDto,user.getUsername())
                .thenReturn(ResponseEntity.ok().build());
    }

    @GetMapping("/memberInfo-pk/{memberPk}")
    Mono<ResponseEntity<MemberInfoResponseDto>> loadMemberByPk(@PathVariable Integer memberPk) {
        log.info("유저 정보 조회 memberPk={}", memberPk);

        return memberService.loadMemberInfoByPk(memberPk)
                .map(response -> ResponseEntity.ok().body(response))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/memberInfo-nick/{nickname}")
    Mono<ResponseEntity<MemberInfoResponseDto>> loadMemberByNickname(@PathVariable String nickname) {
        log.info("유저 정보 조회 member nickname={}", nickname);

        return memberService.loadMemberInfoByNickname(nickname)
                .map(response -> ResponseEntity.ok().body(response))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
