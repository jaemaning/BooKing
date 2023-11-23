package com.booking.chat.chatroom.service;

import com.booking.chat.message.domain.Message;
import com.booking.chat.message.dto.response.MessageResponse;
import com.booking.chat.message.repository.MessageRepository;
import com.booking.chat.chatroom.domain.Chatroom;
import com.booking.chat.chatroom.dto.request.ExitChatroomRequest;
import com.booking.chat.chatroom.dto.request.InitChatroomRequest;
import com.booking.chat.chatroom.dto.request.JoinChatroomRequest;
import com.booking.chat.chatroom.dto.request.LastMessageRequest;
import com.booking.chat.chatroom.dto.response.ChatroomListResponse;
import com.booking.chat.chatroom.exception.ChatroomException;
import com.booking.chat.chatroom.repository.ChatroomRepository;
import com.booking.chat.global.exception.ErrorCode;
import com.booking.chat.kafka.service.KafkaService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final MessageRepository messageRepository;
    private final ReactiveRedisTemplate<String, Set<Long>> reactiveRedisTemplate;
    private final KafkaService kafkaService;

    public Mono<Chatroom> initializeChatroom(InitChatroomRequest initChatroomRequest) {
        return chatroomRepository.findById(initChatroomRequest.meetingId())
                                 .flatMap(existingChatroom -> Mono.<Chatroom>error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chatroom with given meetingId already exists")))
                                 .switchIfEmpty(Mono.defer(() -> {
                                     Chatroom chatroom = Chatroom.createWithLeader(initChatroomRequest);
                                     return chatroomRepository.save(chatroom);
//                                         .flatMap(savedChatroom -> createKafkaTopic(initChatroomRequest.meetingId())
//                                             .thenReturn(savedChatroom));
                                 }));
    }

    private Mono<Void> createKafkaTopic(Long chatroomId) {
        return Mono.fromRunnable(() -> {
            log.info(" kafka topic Chatroom-{} initialize", chatroomId);
            kafkaService.createTopic(chatroomId);
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    public Mono<Long> joinChatroom(JoinChatroomRequest joinChatroomRequest) {
        return chatroomRepository.findById(joinChatroomRequest.meetingId())
                                 .flatMap(chatroom -> {
                                     List<Long> members = chatroom.getMemberList();
                                     if (members.contains(joinChatroomRequest.memberId())) {
                                         return Mono.empty(); // 에러 대신 Mono<Void> 반환
                                     }
                                     members.add(joinChatroomRequest.memberId());
                                     return chatroomRepository.save(chatroom).thenReturn(joinChatroomRequest.memberId());
                                 });
    }

    public Mono<Long> exitChatroom(ExitChatroomRequest exitChatroomRequest) {
        return chatroomRepository.findById(exitChatroomRequest.meetingId())
                                 .flatMap(chatroom -> {
                                     boolean removed = chatroom.getMemberList().remove(exitChatroomRequest.memberId());
                                     if (!removed) {
                                         return Mono.empty(); // 대신 Mono<Void>를 반환
                                     }
                                     return chatroomRepository.save(chatroom).thenReturn(exitChatroomRequest.memberId());
                                 });
    }
    public Flux<ChatroomListResponse> getChatroomListByMemberId(Long memberId) {
        return chatroomRepository.findByMemberListContains(memberId)
                                 .map(ChatroomListResponse::from);
    }

    public Flux<ChatroomListResponse> getChatroomListByMemberIdOrderByDesc(Long memberId) {
        return chatroomRepository.findByMemberListContainsOrderByLastMessageReceivedTimeDesc(memberId)
                                 .map(ChatroomListResponse::from);
    }


    public Mono<Chatroom> findByChatroomId(Long chatroomId) {
        return chatroomRepository.findById(chatroomId).switchIfEmpty(Mono.error(new ChatroomException(ErrorCode.CHATROOM_NOT_FOUND)));
    }

    public Mono<Chatroom> save(Chatroom chatroom) {
        return chatroomRepository.save(chatroom);
    }

    public Mono<String> getChatroomMeetingTitle(Long chatroomId) {
        return chatroomRepository.findById(chatroomId)
                                 .map(Chatroom::getMeetingTitle);
    }

    public Flux<MessageResponse> enterChatroom(Long chatroomId, Long memberId, LastMessageRequest lastMessageRequest) {

        // 1. 입장할 시, 레디스에 저장
        String chatroomKey = "chatroom-%d".formatted(chatroomId);
        Mono<Void> redisUpdateMono = redisManager(chatroomKey, memberId);
        // 2. 마지막 읽은 메세지부터, 마지막 메세지까지 불러오면서, readCount-- 및 읽은 사람 목록에 추가

        Flux<Message> updatedMessagesFlux = messageRepository.findByChatroomIdAndMessageIdGreaterThanEqual(chatroomId, lastMessageRequest.lastMessageIndex())
                                                             .flatMap(message -> {
                                                                 if(message.getReadMemberList().add(memberId)){
                                                                    message.decreaseReadCount();
                                                                 }
                                                                 return messageRepository.save(message);
                                                             });

        // 레디스 업데이트 후 메시지 스트림 반환
        return redisUpdateMono.thenMany(updatedMessagesFlux).map(MessageResponse::new);
    }

    public Mono<Void> disconnectChatroom(Long chatroomId, Long memberId) {

        String chatroomName = "chatroom-%d".formatted(chatroomId);

        return reactiveRedisTemplate.opsForValue().get(chatroomName)
                                    .flatMap(memberList -> {
                                        if(!memberList.remove(memberId)) {
                                            return Mono.error(new ChatroomException(ErrorCode.MEMBER_NOT_PART_OF_CHATROOM));
                                        };
                                        if (memberList.isEmpty()) {
                                            return reactiveRedisTemplate.delete(chatroomName).then();
                                        } else {
                                            return reactiveRedisTemplate.opsForValue().set(chatroomName, memberList).then();
                                        }
                                    })
                                    .then()
            .onErrorResume(error -> Mono.error(new ChatroomException(ErrorCode.MEMBER_NOT_PART_OF_CHATROOM)));
    }

    private Mono<Void> redisManager(String chatroomKey, Long memberId) {
        reactiveRedisTemplate.hasKey(chatroomKey)
                             .publishOn(
                                 Schedulers.boundedElastic()) // 블로킹 작업에 적합한 스레드 사용
                             .flatMap(exists -> {
                                 if (Boolean.FALSE.equals(exists)) {
                                     return storeMemberStatusWithCreateKey(chatroomKey,
                                         memberId);
                                 } else {
                                     return storeMemberStatus(chatroomKey, memberId);
                                 }
                             })
                             .subscribe(
                                 result -> {}, // onNext
                                 error -> log.error("Error on storing member status", error) // onError
                             );

        return Mono.empty();
    }

    private Mono<Boolean> storeMemberStatusWithCreateKey(String chatroomKey, Long memberId) {
        log.info(" {} member connected {} and stored by redis", memberId, chatroomKey);
        Set<Long> memberList = Set.of(memberId);
        return reactiveRedisTemplate.opsForValue()
                                    .set(chatroomKey, memberList);
    }

    private Mono<Boolean> storeMemberStatus(String chatroomKey, Long memberId) {
        log.info(" {} member connected {} by redis", memberId, chatroomKey);
        return reactiveRedisTemplate.opsForValue()
                                    .get(chatroomKey)
                                    .defaultIfEmpty(new HashSet<>())
                                    .doOnNext(memberList -> memberList.add(memberId))
                                    .flatMap(memberList -> reactiveRedisTemplate.opsForValue()
                                                                                .set(chatroomKey, memberList));
    }
}
