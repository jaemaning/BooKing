package com.booking.chat.chatroom.controller;

import com.booking.chat.message.dto.response.MessageResponse;
import com.booking.chat.chatroom.dto.request.ExitChatroomRequest;
import com.booking.chat.chatroom.dto.request.InitChatroomRequest;
import com.booking.chat.chatroom.dto.request.JoinChatroomRequest;
import com.booking.chat.chatroom.dto.request.LastMessageRequest;
import com.booking.chat.chatroom.dto.response.ChatroomListResponse;
import com.booking.chat.chatroom.service.ChatroomService;
import com.booking.chat.global.jwt.JwtUtil;
import com.booking.chat.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/chat/room")
@RestController
public class ChatroomController {

    private final ChatroomService chatroomService;
    private final KafkaService kafkaService;
    private static final String AUTHORIZATION = "Authorization";

    @PostMapping("/")
    public Mono<ResponseEntity<Void>> initializeChatroom(@RequestBody InitChatroomRequest initChatroomRequest) {
        return chatroomService.initializeChatroom(initChatroomRequest)
                              .flatMap(chatroom -> {
                                  log.info(
                                      " Request to create room number : {} , the LeaderId is : {} ",
                                      initChatroomRequest.meetingId(),
                                      initChatroomRequest.leaderId());
                                  return Mono.just(new ResponseEntity<Void>(HttpStatus.CREATED));
                              })
                              .onErrorResume(e -> {
                                  log.info(" Request to create room is failed");
                                  if (e instanceof ResponseStatusException) {

                                      return Mono.just(new ResponseEntity<Void>(((ResponseStatusException) e).getStatus()));
                                  }
                                  return Mono.just(new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR));
                              });
    }

    @PostMapping("/join")
    public Mono<ResponseEntity<Void>> joinChatroom(@RequestBody JoinChatroomRequest joinChatroomRequest) {
        log.info(" {} member request join chatroom : {} ", joinChatroomRequest.memberId(), joinChatroomRequest.meetingId());
        return chatroomService.joinChatroom(joinChatroomRequest)
                              .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chatroom not found or member already exists")))
                              .then(Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }

    @PostMapping("/exit")
    public Mono<ResponseEntity<Void>> exitChatroom(@RequestBody ExitChatroomRequest exitChatroomRequest) {
        log.info(" {} member request exit chatroom : {} ", exitChatroomRequest.memberId(), exitChatroomRequest.meetingId());
        return chatroomService.exitChatroom(exitChatroomRequest)
                              .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chatroom not found or member not part of chatroom")))
                              .then(Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }


    @GetMapping("/list")
    public Flux<ChatroomListResponse> getChatroomListByMemberId(@RequestHeader(AUTHORIZATION) String token) {
        Long memberId = JwtUtil.getMemberIdByToken(token);
        log.info(" {} member request chatroomList ", memberId);
        return chatroomService.getChatroomListByMemberIdOrderByDesc(memberId);
    }

    @PostMapping("/{chatroomId}")
    public Flux<MessageResponse> enterChatroom(@PathVariable Long chatroomId, @RequestHeader(AUTHORIZATION) String token, @RequestBody LastMessageRequest lastMessageRequest){
        Long memberId = JwtUtil.getMemberIdByToken(token);
        log.info(" {} member request enter chatroom : {} , startIdx : {} ", memberId, chatroomId, lastMessageRequest.lastMessageIndex());
        return chatroomService.enterChatroom(chatroomId, memberId, lastMessageRequest);
    }

    @DeleteMapping("/{chatroomId}")
    public Mono<ResponseEntity<Void>> disconnectChatroom(@PathVariable Long chatroomId, @RequestHeader(AUTHORIZATION) String token) {
        Long memberId = JwtUtil.getMemberIdByToken(token);
        log.info(" {} member request disconnect chatroom : {} ", memberId, chatroomId);

        return chatroomService.disconnectChatroom(chatroomId, memberId)
                                .then(Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }


}
