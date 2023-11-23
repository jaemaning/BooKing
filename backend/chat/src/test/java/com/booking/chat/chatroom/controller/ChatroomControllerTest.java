package com.booking.chat.chatroom.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

import com.booking.chat.message.dto.response.MessageResponse;
import com.booking.chat.chatroom.domain.Chatroom;
import com.booking.chat.chatroom.dto.request.ExitChatroomRequest;
import com.booking.chat.chatroom.dto.request.InitChatroomRequest;
import com.booking.chat.chatroom.dto.request.JoinChatroomRequest;
import com.booking.chat.chatroom.dto.request.LastMessageRequest;
import com.booking.chat.chatroom.dto.response.ChatroomListResponse;
import com.booking.chat.util.ControllerTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class ChatroomControllerTest extends ControllerTest {

    private final String BASE_URL = "/api/chat/room";

    @BeforeEach
    void setUp() {
        this.webTestClient = WebTestClient.bindToController(new ChatroomController(chatroomService))
                                          .configureClient()
                                          .filter(
                                              documentationConfiguration(this.restDocumentation))
                                          .build();
    }

    @DisplayName("모임이 생성되면 채팅방 생성된다")
    @Test
    void initializeChatroomTest() throws Exception {

        InitChatroomRequest initChatroomRequest = new InitChatroomRequest(1L, 1L, "미팅 이름");
        Chatroom chatroom = Chatroom.createWithLeader(initChatroomRequest);
        when(chatroomService.initializeChatroom(any())).thenReturn(Mono.just(chatroom));

        webTestClient.post()
                     .uri(BASE_URL + "/")
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(objectMapper.writeValueAsString(initChatroomRequest))
                     .exchange()
                     .expectStatus()
                     .isCreated()
                     .expectBody()
                     .consumeWith(document("chatroom/init",
                         preprocessRequest(prettyPrint()),
                         requestFields(
                             fieldWithPath("meetingId").type(JsonFieldType.NUMBER)
                                                       .description("모임 PK"),
                             fieldWithPath("leaderId").type(JsonFieldType.NUMBER)
                                                      .description("모임장 PK"),
                             fieldWithPath("meetingTitle").type(JsonFieldType.STRING)
                                                          .description("모임 제목")
                         )
                     ));
    }

    @DisplayName("회원 채팅방 가입한다")
    @Test
    void joinChatroomTest() throws Exception {

        JoinChatroomRequest joinChatroomRequest = new JoinChatroomRequest(1L, 1L);
        when(chatroomService.joinChatroom(any())).thenReturn(Mono.just(1L));

        webTestClient.post()
                     .uri(BASE_URL + "/join")
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(objectMapper.writeValueAsString(joinChatroomRequest))
                     .exchange()
                     .expectStatus()
                     .isNoContent()
                     .expectBody()
                     .consumeWith(document("chatroom/join",
                         preprocessRequest(prettyPrint()),
                         requestFields(
                             fieldWithPath("meetingId").type(JsonFieldType.NUMBER)
                                                       .description("모임 PK"),
                             fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                                                      .description("멤버 PK")
                         )
                     ));
    }

    @DisplayName("회원 채팅방 탈퇴한다")
    @Test
    void exitChatroomTest() throws Exception {

        ExitChatroomRequest exitChatroomRequest = new ExitChatroomRequest(1L, 1L);
        when(chatroomService.exitChatroom(any())).thenReturn(Mono.just(1L));

        webTestClient.post()
                     .uri(BASE_URL + "/exit")
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(objectMapper.writeValueAsString(exitChatroomRequest))
                     .exchange()
                     .expectStatus()
                     .isNoContent()
                     .expectBody()
                     .consumeWith(document("chatroom/exit",
                         preprocessRequest(prettyPrint()),
                         requestFields(
                             fieldWithPath("meetingId").type(JsonFieldType.NUMBER)
                                                       .description("모임 PK"),
                             fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                                                      .description("멤버 PK")
                         )
                     ));
    }

    @DisplayName("회원 채팅방 조회된다")
    @Test
    void getChatroomListTest() throws Exception {

        ChatroomListResponse chatroomListResponse = new ChatroomListResponse(1L, 1L, "meetingTitle","lastMessage", "image",
            List.of(1L, 2L, 3L));

        ChatroomListResponse chatroomListResponse2 = new ChatroomListResponse(2L, 1L, "meetingTitle2","lastMessage2", "image",
            List.of(1L, 2L, 3L));

        when(chatroomService.getChatroomListByMemberIdOrderByDesc(any())).thenReturn(
            Flux.just(chatroomListResponse, chatroomListResponse2));

        webTestClient.get()
                     .uri(BASE_URL + "/list")
                     .header("Authorization", "Bearer: token")
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody()
                     .consumeWith(document("chatroom/list",
                         preprocessResponse(prettyPrint()),
                         responseFields(
                             fieldWithPath("[]").description("속한 채팅방 목록 dto"),
                             fieldWithPath("[].chatroomId").type(JsonFieldType.NUMBER).description("채팅방 PK"),
                             fieldWithPath("[].lastMessageIdx").type(JsonFieldType.NUMBER).description("마지막 메세지 PK"),
                             fieldWithPath("[].meetingTitle").type(JsonFieldType.STRING).description("미팅방 이름"),
                             fieldWithPath("[].lastMessage").type(JsonFieldType.STRING).description("마지막 수신된 메세지"),
                             fieldWithPath("[].memberList[]").type(JsonFieldType.ARRAY).description("채팅방 멤버들 PK 배열")
                         )
                     ));
    }

    @DisplayName("회원 채팅방 입장하면, 마지막 읽은 메세지부터 전송한다")
    @Test
    void enterChatroomTest() throws Exception {

        LastMessageRequest lastMessageRequest = new LastMessageRequest(1L);

        MessageResponse messageResponse1 = new MessageResponse(1L, 2L , 3L, "HELLO WEB FLUX!!!!!", 2, LocalDateTime.now());
        MessageResponse messageResponse2 = new MessageResponse(2L, 4L , 3L, "BYE WEB FLUX!!!!!", 5, LocalDateTime.now());

        when(chatroomService.enterChatroom(any(), any(), any())).thenReturn(Flux.just(messageResponse1, messageResponse2));

        webTestClient.post()
            .uri(BASE_URL + "/{chatroomId}", 1L)
                     .header("Authorization", "Bearer: token")
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(objectMapper.writeValueAsString(lastMessageRequest))
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody()
                     .consumeWith(document("chatroom/enter",
                         preprocessRequest(prettyPrint()),
                         preprocessResponse(prettyPrint()),
                         pathParameters(
                            parameterWithName("chatroomId").description("채팅방 PK")
                         ),
                         responseFields(
                             fieldWithPath("[]").description("요청한 시점부터의 마지막까지의 메세지"),
                             fieldWithPath("[].chatroomId").type(JsonFieldType.NUMBER).description("채팅방 PK"),
                             fieldWithPath("[].messageId").type(JsonFieldType.NUMBER).description("메세지 ID"),
                             fieldWithPath("[].senderId").type(JsonFieldType.NUMBER).description("메세지 보낸 회원 PK"),
                             fieldWithPath("[].content").type(JsonFieldType.STRING).description("메세지 내용"),
                             fieldWithPath("[].readCount").type(JsonFieldType.NUMBER).description("안읽은 사람 수"),
                             fieldWithPath("[].timestamp").description("메세지 전송 시각")
                         ),
                         requestFields(
                             fieldWithPath("lastMessageIndex").type(JsonFieldType.NUMBER).description("회원의 마지막 읽은 메세지")
                         )
                     ));
    }

    @DisplayName("회원 채팅방 소켓 끊으면, 접속 목록에서 삭제된다")
    @Test
    void disconnectChatroomTest() throws Exception {

        when(chatroomService.disconnectChatroom(any(), any())).thenReturn(Mono.empty());

        webTestClient.delete()
                     .uri(BASE_URL + "/{chatroomId}", 1L)
                     .header("Authorization", "Bearer: token")
                     .exchange()
                     .expectStatus()
                     .isNoContent()
                        .expectBody()
                        .consumeWith(document("chatroom/disconnect",
                            pathParameters(
                                parameterWithName("chatroomId").description("채팅방 PK")
                            )));
    }
}