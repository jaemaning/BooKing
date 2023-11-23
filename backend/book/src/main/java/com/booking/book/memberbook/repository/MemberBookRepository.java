package com.booking.book.memberbook.repository;

import com.booking.book.memberbook.domain.MemberBook;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MemberBookRepository extends ReactiveMongoRepository<MemberBook, Long> {

    Flux<MemberBook> findAllByMemberPkOrderByCreatedAtDesc(Integer memberPk);

    Mono<MemberBook> findByMemberPkAndBookIsbn(Integer memberPk, String isbn);

    Mono<MemberBook> findBy_id(String memberBookId);
}
