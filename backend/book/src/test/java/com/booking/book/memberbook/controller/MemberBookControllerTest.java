package com.booking.book.memberbook.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

import com.booking.book.book.dto.response.BookResponse;
import com.booking.book.memberbook.domain.MemberBook;
import com.booking.book.memberbook.domain.Note;
import com.booking.book.memberbook.dto.request.MemberBookRegistRequest;
import com.booking.book.memberbook.dto.response.MemberBookResponse;
import com.booking.book.util.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class MemberBookControllerTest extends ControllerTest {

    private final String BASE_URL = "/api/book/member";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.webTestClient = WebTestClient.bindToController(new MemberBookController(memberBookService))
                .configureClient()
                .filter(
                        documentationConfiguration(this.restDocumentation))
                .build();
    }

    @DisplayName("내 서재 책 등록")
    @Test
    void t1() throws Exception {
        MemberBookRegistRequest request=new MemberBookRegistRequest("닉네임","isbn");
        List<Note> notes=new ArrayList<>();
        notes.add(new Note("메모", LocalDateTime.now()));
        MemberBook memberBook=new MemberBook("1","멤버 닉네임","책 isbn",notes,LocalDateTime.now());

        when(memberBookService.registerMemberBook(any())).thenReturn(Mono.just(memberBook));

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path(BASE_URL + "/").build())
                .header("Authorization","Bearer JWT")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .consumeWith(document("memberBook/register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("bookIsbn").type(JsonFieldType.STRING).description("책 isbn")
                        ))
                );
    }

    @DisplayName("내 서재 리스트 조회")
    @Test
    void t2() throws Exception {
//        MemberBookRegistRequest request=new MemberBookRegistRequest("닉네임","isbn");
        List<Note> notes=new ArrayList<>();
        notes.add(new Note("메모", LocalDateTime.now()));
        MemberBook memberBook=new MemberBook("1","멤버 닉네임","책 isbn",notes,LocalDateTime.now());
        BookResponse bookResponse=new BookResponse("title","author","coverImage","genre",LocalDate.now(),"content","isbn");
        MemberBookResponse memberBookResponse=new MemberBookResponse("nickname",bookResponse,notes,LocalDateTime.now());

        when(memberBookService.getMemberBookByMemberId(any())).thenReturn(Flux.just(memberBookResponse));

        FieldDescriptor[] response = new FieldDescriptor[]{
                fieldWithPath("[].memberNickname").type(JsonFieldType.STRING).description("멤버 닉네임"),
                subsectionWithPath("[].bookInfo").type(JsonFieldType.OBJECT).description("책 정보"),
                fieldWithPath("[].bookInfo.title").type(JsonFieldType.STRING).description("책 제목"),
                fieldWithPath("[].bookInfo.author").type(JsonFieldType.STRING).description("책 작가"),
                fieldWithPath("[].bookInfo.coverImage").type(JsonFieldType.STRING).description("책 커버 이미지"),
                fieldWithPath("[].bookInfo.genre").type(JsonFieldType.STRING).description("책 장르"),
                fieldWithPath("[].bookInfo.publishDate").type(JsonFieldType.ARRAY).description("책 출판일"),
                fieldWithPath("[].bookInfo.content").type(JsonFieldType.STRING).description("책 내용"),
                fieldWithPath("[].bookInfo.isbn").type(JsonFieldType.STRING).description("책 ISBN"),
                subsectionWithPath("[].notes").type(JsonFieldType.ARRAY).description("메모들"),
                fieldWithPath("[].notes[].memo").type(JsonFieldType.STRING).description("메모 내용"),
                fieldWithPath("[].notes[].createdAt").type(JsonFieldType.ARRAY).description("메모 생성 일시"),
                fieldWithPath("[].createdAt").type(JsonFieldType.ARRAY).description("내 서재 등록 일시")
        };

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URL + "/{nickname}").build("닉네임"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(document("memberBook/getList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                response
                        ))
                );
    }

    @DisplayName("내 서재 디테일 조회")
    @Test
    void t3() throws Exception {
//        MemberBookRegistRequest request=new MemberBookRegistRequest("닉네임","isbn");
        List<Note> notes=new ArrayList<>();
        notes.add(new Note("메모", LocalDateTime.now()));
        MemberBook memberBook=new MemberBook("1","멤버 닉네임","책 isbn",notes,LocalDateTime.now());
        BookResponse bookResponse=new BookResponse("title","author","coverImage","genre",LocalDate.now(),"content","isbn");
        MemberBookResponse memberBookResponse=new MemberBookResponse("nickname",bookResponse,notes,LocalDateTime.now());

        when(memberBookService.getMemberBookDetail(anyString(),anyString())).thenReturn(Mono.just(memberBookResponse));

        FieldDescriptor[] response = new FieldDescriptor[]{
                fieldWithPath("memberNickname").type(JsonFieldType.STRING).description("멤버 닉네임"),
                subsectionWithPath("bookInfo").type(JsonFieldType.OBJECT).description("책 정보"),
                fieldWithPath("bookInfo.title").type(JsonFieldType.STRING).description("책 제목"),
                fieldWithPath("bookInfo.author").type(JsonFieldType.STRING).description("책 작가"),
                fieldWithPath("bookInfo.coverImage").type(JsonFieldType.STRING).description("책 커버 이미지"),
                fieldWithPath("bookInfo.genre").type(JsonFieldType.STRING).description("책 장르"),
                fieldWithPath("bookInfo.publishDate").type(JsonFieldType.ARRAY).description("책 출판일"),
                fieldWithPath("bookInfo.content").type(JsonFieldType.STRING).description("책 내용"),
                fieldWithPath("bookInfo.isbn").type(JsonFieldType.STRING).description("책 ISBN"),
                subsectionWithPath("notes").type(JsonFieldType.ARRAY).description("메모들"),
                fieldWithPath("notes[].memo").type(JsonFieldType.STRING).description("메모 내용"),
                fieldWithPath("notes[].createdAt").type(JsonFieldType.ARRAY).description("메모 생성 일시"),
                fieldWithPath("createdAt").type(JsonFieldType.ARRAY).description("내 서재 등록 일시")
        };

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URL + "/{nickname}/{isbn}").build("닉네임","isbn"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(document("memberBook/getDetail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                response
                        ))
                );
    }

}