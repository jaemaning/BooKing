package com.booking.chat.test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.booking.chat.kafka.domain.KafkaMessage;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class SimpleKafkaTest {

    @Autowired
    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;
    private CountDownLatch latch = new CountDownLatch(1);
    private KafkaMessage consumedMessage;

    @Test
    @DisplayName("Kafka test 수행된다")
    public void t1() throws Exception {

        KafkaMessage message = KafkaMessage.builder()
                                           .message("Test Message")
                                           .senderId(2L)
                                           .sendTime(LocalDateTime.now())
                                           .build();

        kafkaTemplate.send("test-topic", message);

        latch.await(60, TimeUnit.SECONDS); // 여기에서 특정 시간 동안 메시지 수신을 기다립니다.
        assertThat(consumedMessage).isEqualToComparingFieldByField(message);
    }

    @KafkaListener(topics = "test-topic")
    public void listen(KafkaMessage message) {
        consumedMessage = message;
        latch.countDown();
    }

}
