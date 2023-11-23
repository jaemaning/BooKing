package com.booking.chat.kafka.service;

import com.booking.chat.kafka.domain.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatListenerService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @KafkaListener(topics = "Chat", groupId = "chat", containerFactory = "kafkaListenerContainerFactory")
    public void listenGroupChat(KafkaMessage message, @Header("chatroomId") String chatroomIdStr) {
        Long chatroomId = Long.valueOf(chatroomIdStr);

        log.info("kafka event publish to Chatroom-{}", chatroomId);
        simpMessagingTemplate.convertAndSend("/subscribe/" + chatroomId, message); // WebSocket을 통해 클라이언트에게 메세지 전송
    }
}
