package com.booking.book.memberbook.controller;

import com.booking.book.memberbook.dto.request.MemberBookRegistRequest;
import com.booking.book.memberbook.dto.request.RegisterNoteRequest;
import com.booking.book.memberbook.dto.response.MemberBookResponse;
import com.booking.book.memberbook.service.MemberBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/book/member")
@RestController
public class MemberBookController {

    private final MemberBookService memberBookService;
    private static final String AUTHORIZATION = "Authorization";

    @GetMapping("/{memberPk}")
    public Flux<MemberBookResponse> getMemberBookByMemberId(@PathVariable Integer memberPk) {
//        Long memberId = JwtUtil.getMemberIdByToken(token);
        log.info(" {} member request readBookList", memberPk);
        return memberBookService.getMemberBookByMemberId(memberPk);
    }

    @GetMapping("/{memberPk}/{isbn}")
    public Mono<ResponseEntity<MemberBookResponse>> getMemberBookDetail(@PathVariable Integer memberPk,@PathVariable String isbn) {
//        Long memberId = JwtUtil.getMemberIdByToken(token);
        log.info(" {} member request detail member book : {} ", memberPk, isbn);
        return memberBookService.getMemberBookDetail(memberPk, isbn)
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Member has not read this book")))
                                .map(ResponseEntity::ok);
    }

    @PostMapping("/")
    public Mono<ResponseEntity<Void>> registerMemberBook(@RequestHeader(AUTHORIZATION) String token,@RequestBody MemberBookRegistRequest memberBookRegistRequest) {
        log.info(" {} member register book : {} ", memberBookRegistRequest.memberPk(), memberBookRegistRequest.bookIsbn());
        return memberBookService.registerMemberBook(memberBookRegistRequest)
            .flatMap(memberBook -> {
                return Mono.just(new ResponseEntity<Void>(HttpStatus.CREATED));
            })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
    }

    @PostMapping("/note")
    public Mono<ResponseEntity<String>> registerNote(@RequestHeader(AUTHORIZATION) String token,
                                                @RequestBody RegisterNoteRequest request) {
        log.info("한줄평 등록 요청 {}",request.toString());
        return memberBookService.registerNote(request)
                .flatMap(resp->Mono.just(ResponseEntity.ok().body(resp)))
                .onErrorResume(e->{
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(e.getMessage()));
                });
    }

    @DeleteMapping("/{memberBookId}")
    public Mono<ResponseEntity<String>> deleteMemberBook(@RequestHeader(AUTHORIZATION) String token,
                                                    @PathVariable String memberBookId) {
        log.info("내 서재 책 삭제 요청 : {}",memberBookId);
        return memberBookService.deleteMemberBook(memberBookId)
                .flatMap(resp -> Mono.just(ResponseEntity.ok().body(resp)))
                .onErrorResume(e->{
                    log.error("내 서재 책 삭제 요청 에러 : {}",e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(e.getMessage()));
                });
    }


    @DeleteMapping("/{memberBookId}/{noteIndex}")
    public Mono<ResponseEntity<String>> deleteMemberBook(@RequestHeader(AUTHORIZATION) String token,
                                                         @PathVariable String memberBookId,
                                                         @PathVariable Integer noteIndex) {
        log.info("내 서재 memberBookId: {}, 한줄평 삭제 요청 : {}",memberBookId,noteIndex);
        return memberBookService.deleteNote(memberBookId,noteIndex)
                .flatMap(resp -> Mono.just(ResponseEntity.ok().body(resp)))
                .onErrorResume(e->{
                    log.error("한줄평 삭제 요청 에러 : {}",e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body(e.getMessage()));
                });
    }
}
