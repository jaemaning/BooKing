package com.booking.book.book.repository;

import com.booking.book.book.domain.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {

    @Query(value = "{ '$text': { $search: ?0 } }",
        sort = "{ score: { $meta: 'textScore' } }")
    Flux<Book> findByTitleContaining(String title, Pageable pageable);

    Flux<Book> findByTitleRegex(String regex);

    @Query("{ 'publishDate' : { $lt: ?0 } }")
    Flux<Book> findByPublishDateBeforeCurrentDateOrderByPublishDateDesc(LocalDateTime currentDate, Pageable pageable);

    Flux<Book> findTop30ByOrderByMeetingCntDesc();
}
