package com.booking.book.book.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.booking.book.book.dto.response.BookResponse;
import com.booking.book.book.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


@ActiveProfiles("test")
@SpringBootTest
class BookServiceTest {

    @Autowired BookService bookService;
    @Autowired BookRepository bookRepository;

//    @BeforeEach
//    void setUp() {
//            bookRepository.findAll()
//                          .thenMany(bookRepository.findAll()) // 모든 책을 찾아서
//                          .map(Book::getTitle) // 제목을 가져오고
//                          .flatMap(title -> bookService.ensureTextIndexOnTitle()) // text index 생성
//                          .blockLast(); // 마지막까지 블록, 모든 인덱스 생성 완료 대기
//        }

    @DisplayName("책 제목으로 검색된다")
    @Test
    void searchBookListByTitle() throws Exception {

        String title = "동물";

        Flux<BookResponse> bookList = bookService.searchBookListByTitleAndRelevance(title);

        StepVerifier.create(bookList)
                    .assertNext(bookResponse -> {
                        System.out.println(bookResponse.title());
                        assertNotNull(bookResponse.title());
                        assertTrue(bookResponse.title().contains(title));
                    })
                    .verifyComplete();
//        assertNotNull(bookList);
    }
}