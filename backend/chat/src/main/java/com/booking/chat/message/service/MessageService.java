package com.booking.chat.message.service;


import com.booking.chat.message.domain.Message;
import com.booking.chat.message.repository.MessageRepository;
import com.booking.chat.chatroom.domain.Chatroom;
import com.booking.chat.chatroom.service.ChatroomService;
import com.booking.chat.kafka.domain.KafkaMessage;
import com.booking.chat.notification.dto.response.NotificationResponse;
import com.booking.chat.notification.service.NotificationService;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatroomService chatroomService;
    private final ReactiveKafkaProducerTemplate<String, KafkaMessage> reactiveKafkaProducerTemplate;
    private final NotificationService notificationService;
    private final ReactiveRedisTemplate<String, Set<Long>> reactiveRedisTemplate;
    // reactive로 대체
    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    public Mono<Void> processAndSend(KafkaMessage kafkaMessage, Long chatroomId) {
        return save(kafkaMessage, chatroomId)
            .retry(3L)
            .then(Mono.defer(() -> proceedMessageSendProcess(kafkaMessage, chatroomId)))
            .doOnError(x -> log.info(" optimistic error by {} ", x.toString()))
            .then();
    }

    private Mono<Void> proceedMessageSendProcess(KafkaMessage kafkaMessage, Long chatroomId) {
       return chatroomService.findByChatroomId(chatroomId)
                       .flatMapMany(chatroom -> {
                           String meetingTitle = chatroom.getMeetingTitle();
                           String message = kafkaMessage.getMessage();
                           String memberName = kafkaMessage.getSenderName();
                           List<Long> notificationList = new ArrayList<>(chatroom.getMemberList());
                           notificationList.remove(kafkaMessage.getSenderId());

                           String chatroomKey = "chatroom-%d".formatted(chatroomId);
                           return reactiveRedisTemplate.opsForValue().get(chatroomKey)
                                                       .switchIfEmpty(Mono.just(new HashSet<>())) // 키가 없으면 빈 해시셋 반환
                                                       .map(onlineMembers -> {
                                                           Set<Long> onlineMemberSet = new HashSet<>(onlineMembers);
                                                           notificationList.removeIf(onlineMemberSet::contains); // 접속 중인 사용자 제외
                                                           return notificationList;
                                                       })
                                                       // 필터링된 멤버리스트를 Flux로 변환하여 알림 전송
                                                       .flatMapMany(Flux::fromIterable)
                                                       .flatMap(memberId -> notificationService.sendChattingNotification(new NotificationResponse(meetingTitle, message, memberName, memberId, kafkaMessage.extractData(chatroomId))), 5)
                                                       .thenMany(Flux.just(chatroom)); // 작업을 이어갈 Flux 반환
                       })
                       .then(sendMessageToKafka(kafkaMessage, chatroomId)); // 모든 알림이 완료되면 Kafka로 메시지 전송을 계속함
                       //.subscribe();
    }
    private Mono<Void> sendMessageToKafka(KafkaMessage kafkaMessage, Long chatroomId) {
        // Kafka로 메세지와 함께 채팅방 ID를 헤더에 추가하여 전달
        ProducerRecord<String, KafkaMessage> record = new ProducerRecord<>("Chat", null, null, kafkaMessage);
        record.headers().add("chatroomId", chatroomId.toString().getBytes(StandardCharsets.UTF_8));
        return reactiveKafkaProducerTemplate.send(record)
                                            .doOnSuccess(s -> log.info("Kafka message publish success to {}", chatroomId))
                                            .doOnError(e -> log.error("Error publishing message to Kafka", e))
                                            .then();
    }

    public Mono<Void> save(KafkaMessage message, Long chatroomId) {
        return chatroomService.findByChatroomId(chatroomId)
                              .flatMap(chatroom -> {
                                  Long idx = chatroom.getMessageIndex();
                                  chatroom.updateIndex();
                                  return getReadCount(chatroom, message.getSenderId())
                                      .flatMap(readCount ->
                                          chatroomService.save(chatroom)
                                                         .then(Mono.just(Message.builder()
                                                                                .chatroomId(chatroomId)
                                                                                .messageId(idx)
                                                                                .memberId(message.getSenderId())
                                                                                .memberList(chatroom.getMemberList())
                                                                                .readMemberList(Set.of(message.getSenderId()))
                                                                                .content(message.getMessage())
                                                                                .readCount(readCount)
                                                                                .build()))
                                                         .flatMap(messageRepository::save)
                                      ).flatMap(savedMessage -> {
                                          chatroom.updateListMessageReceived();
                                          chatroom.updateLastMessage(savedMessage.getContent());
                                          return chatroomService.save(chatroom);
                                      });
                              })
                              .then();
    }

    public Flux<Message> findAllByRoomId(Long roomId) {

        return messageRepository.findByChatRoomId(roomId);
    }

    private Mono<Integer> getReadCount(Chatroom chatroom, Long senderId) {
        String chatroomKey = "chatroom-%d".formatted(chatroom.get_id());
        return reactiveRedisTemplate.opsForValue().get(chatroomKey)
                                    .flatMap(memberList -> {
                                        if (memberList != null) {
                                            long count = memberList.stream()
                                                                   .filter(memberId -> memberId.toString().equals(senderId.toString()))
                                                                   .count();
                                            return Mono.just((int) count);
                                        } else {
                                            return Mono.just(chatroom.getMemberList().size() - 1);
                                        }
                                    })
                                    .defaultIfEmpty(chatroom.getMemberList().size() - 1);
    }
}
