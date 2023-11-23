package com.booking.chat.socket.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.booking.chat.kafka.domain.KafkaMessage;
import com.booking.chat.kafka.domain.MessagePayload;
import com.booking.chat.socket.util.MessageFrameHandler;
import com.booking.chat.socket.util.StompTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

public class WebSocketTest extends StompTest {

    @Test
    void t1() throws Exception {

        MessageFrameHandler<MessagePayload[]> handler = new MessageFrameHandler<>(
            MessagePayload[].class);
        this.stompSession.subscribe("/booking/chat", handler);
        this.stompSession.subscribe("/subscribe/1", handler);
        System.out.println("소켓 연결 여부 : " + this.stompSession.isConnected());

        KafkaMessage kafkaMessage = new KafkaMessage("메세지 전송", 2L, LocalDateTime.now(), "희창");
        String chatRoomId = "1";

        MessagePayload messagePayload = new MessagePayload(kafkaMessage, chatRoomId);

        this.stompSession.send("/publish/message", messagePayload);
//        List<MessagePayload> receivedMessages = List.of(handler.getCompletableFuture().get(10, TimeUnit.SECONDS));

        Thread.sleep(1000L);
        List<MessagePayload[]> receivedMessages = handler.getReceivedMessages();

//        receivedMessages.forEach(x -> System.out.println(x.getKafkaMessage().getMessage()));

        assertEquals(1, receivedMessages.size());

    }


}
