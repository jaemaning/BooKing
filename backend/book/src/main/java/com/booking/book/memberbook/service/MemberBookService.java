package com.booking.book.memberbook.service;

import com.booking.book.book.exception.BookException;
import com.booking.book.book.service.BookService;
import com.booking.book.global.exception.ErrorCode;
import com.booking.book.memberbook.domain.MemberBook;
import com.booking.book.memberbook.domain.Note;
import com.booking.book.memberbook.dto.request.MemberBookRegistRequest;
import com.booking.book.memberbook.dto.request.RegisterNoteRequest;
import com.booking.book.memberbook.dto.response.MemberBookResponse;
import com.booking.book.memberbook.repository.MemberBookRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberBookService {

    private final MemberBookRepository memberBookRepository;
    private final BookService bookService;

    public Flux<MemberBookResponse> getMemberBookByMemberId(Integer memberPk) {
        return memberBookRepository.findAllByMemberPkOrderByCreatedAtDesc(memberPk)
                                   .flatMap(memberBook ->
                                       bookService.findByIsbn(memberBook.getBookIsbn())
                                                  .map(book->new MemberBookResponse(memberBook,book))
                                   );
    }

    public Mono<MemberBookResponse> getMemberBookDetail(Integer memberPk, String isbn) {
        return memberBookRepository.findByMemberPkAndBookIsbn(memberPk, isbn)
                .flatMap(memberBook ->
                    bookService.findByIsbn(memberBook.getBookIsbn())
                            .map(book->new MemberBookResponse(memberBook,book))
                );
    }

    public Mono<MemberBook> registerMemberBook(MemberBookRegistRequest memberBookRegistRequest) {
        Integer memberPk= memberBookRegistRequest.memberPk();
        String isbn=memberBookRegistRequest.bookIsbn();
        return memberBookRepository.findByMemberPkAndBookIsbn(memberPk,isbn)
                .flatMap(exist -> {
                    log.error("내 서재 등록 요청 에러 : 이미 등록된 책");
                    return Mono.<MemberBook>error(new RuntimeException("이미 등록된 책"));
                })
                .switchIfEmpty(
                        bookService.findByIsbn(isbn)
                                .flatMap(book -> {
                                    MemberBook memberBook=MemberBook.from(memberBookRegistRequest);
                                    return memberBookRepository.save(memberBook);
                                })
                )
                .onErrorResume(e->{
                    if(e instanceof BookException){
                        log.error("없는 책");
                        return Mono.error(new BookException(ErrorCode.BOOK_NOT_FOUND));
                    }
                    else {
                        return Mono.error(new RuntimeException("이미 등록된 책"));
                    }
                });

    }

    public Mono<String> registerNote(RegisterNoteRequest request) {
        return memberBookRepository.findByMemberPkAndBookIsbn(request.memberPk(), request.isbn())
                .flatMap(memberBook -> {
                    memberBook.getNotes().add(new Note(request.content(), LocalDateTime.now()));
                    return memberBookRepository.save(memberBook);
                })
                .thenReturn("created")
                .onErrorResume(e->{
                    log.error(e.getMessage());
                    return Mono.error(new RuntimeException(e.getMessage()));
                });
    }

    public Mono<String> deleteMemberBook(String memberBookId) {
        return memberBookRepository.findBy_id(memberBookId)
                .flatMap(memberBookRepository::delete)
                .switchIfEmpty(Mono.error(new NotFoundException("Not found memberBook")))
                .thenReturn("deleted");

    }

    public Mono<String> deleteNote(String memberBookId,Integer noteIndex) {
        return memberBookRepository.findBy_id(memberBookId)
                .flatMap(memberBook -> {
                    memberBook.getNotes().remove((int)noteIndex);
                    return memberBookRepository.save(memberBook);
                })
                .switchIfEmpty(Mono.error(new NotFoundException("Not found note")))
                .thenReturn("deleted");
    }
}
