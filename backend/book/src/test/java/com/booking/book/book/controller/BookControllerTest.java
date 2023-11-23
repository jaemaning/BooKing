package com.booking.book.book.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

import com.booking.book.book.domain.Book;
import com.booking.book.book.dto.response.BookResponse;
import com.booking.book.global.WebConfig;
import com.booking.book.util.ControllerTest;

import java.time.LocalDate;
import java.util.Locale;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.web.reactive.result.view.View;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Import(WebConfig.class)
class BookControllerTest extends ControllerTest {

    private final String BASE_URL = "/api/book";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.webTestClient = WebTestClient.bindToController(new BookController(bookService))
                .configureClient()
                .filter(
                        documentationConfiguration(this.restDocumentation))
                .build();
    }

    @DisplayName("책 제목으로 목록 검색 된다")
    @Test
    void searchBookListByTitleParameter() throws Exception {
        BookResponse bookResponse = new BookResponse("이기적 유전자", "작가", "이미지", "장르", LocalDate.now(), "내용", "isbn");
        BookResponse bookResponse2 = new BookResponse("이기적 유전자", "작가", "이미지", "장르", LocalDate.now(), "내용", "isbn");

        when(bookService.searchBookListByTitleAndRelevance(any())).thenReturn(Flux.just(bookResponse, bookResponse2));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URL + "/searchByTitle").queryParam("title", "이기적 유전자").build())
                .attribute("title", "이기적 유전자")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(document("book/listByTitle",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("책 제목"),
                                fieldWithPath("[].author").type(JsonFieldType.STRING).description("작가"),
                                fieldWithPath("[].coverImage").type(JsonFieldType.STRING).description("책 커버 이미지"),
                                fieldWithPath("[].genre").type(JsonFieldType.STRING).description("책 장르"),
                                fieldWithPath("[].publishDate").description("책 출판일"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING).description("책 소개"),
                                fieldWithPath("[].isbn").type(JsonFieldType.STRING).description("ISBN")
                        ))
                );
    }

    @DisplayName("책 ISBN으로 검색 된다")
    @Test
    void t2() throws Exception {
        BookResponse bookResponse = new BookResponse("이기적 유전자", "작가", "이미지", "장르", LocalDate.now(), "내용", "isbn");
        Book bookEntity = new Book("isbn", "이기적 유전자", "작가", "이미지", "장르", LocalDate.now(), "내용", (float) 0);

        when(bookService.findByIsbn(anyString())).thenReturn(Mono.just(bookEntity));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URL + "/searchByIsbn").queryParam("isbn", "646342").build())
                .attribute("isbn", "646342")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(document("book/listByIsbn",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("책 제목"),
                                fieldWithPath("author").type(JsonFieldType.STRING).description("작가"),
                                fieldWithPath("coverImage").type(JsonFieldType.STRING).description("책 커버 이미지"),
                                fieldWithPath("genre").type(JsonFieldType.STRING).description("책 장르"),
                                fieldWithPath("publishDate").description("책 출판일"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("책 소개"),
                                fieldWithPath("isbn").type(JsonFieldType.STRING).description("ISBN")
                        ))
                );
    }

//    @DisplayName("신작 도서 목록")
//    @Test
//    void t3() throws Exception {
//        BookResponse bookResponse = new BookResponse("이기적 유전자", "작가", "이미지", "장르", LocalDate.now(), "내용", "isbn");
//        BookResponse bookResponse2 = new BookResponse("이기적 유전자", "작가", "이미지", "장르", LocalDate.now(), "내용", "isbn");
//        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "publishDate"));
//
//        when(bookService.loadLatestBooks(eq(pageable))).thenReturn(Flux.just(bookResponse, bookResponse2));
//
//        webTestClient.get()
//                .uri(uriBuilder -> uriBuilder.path(BASE_URL + "/latest").queryParam("page", 0).queryParam("size", 20).build())
//                .attribute("page", 0)
//                .attribute("size", 20)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody()
//                .consumeWith(document("book/latest",
//                        preprocessResponse(prettyPrint()),
//                        responseFields(
//                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("책 제목"),
//                                fieldWithPath("[].author").type(JsonFieldType.STRING).description("작가"),
//                                fieldWithPath("[].coverImage").type(JsonFieldType.STRING).description("책 커버 이미지"),
//                                fieldWithPath("[].genre").type(JsonFieldType.STRING).description("책 장르"),
//                                fieldWithPath("[].publishDate").description("책 출판일"),
//                                fieldWithPath("[].content").type(JsonFieldType.STRING).description("책 소개"),
//                                fieldWithPath("[].isbn").type(JsonFieldType.STRING).description("ISBN")
//                        ))
//                );
//    }

}