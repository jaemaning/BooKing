package com.booking.chat.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaService {

    private final KafkaAdmin kafkaAdmin;

    public void createTopic(Long chatroomId, int partitions, short replicationFactor) {
        String topicName = "Chatroom-%d".formatted(chatroomId);
        NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);

        kafkaAdmin.createOrModifyTopics(newTopic);
        log.info("new Topic : {} ", topicName);
        // send(chatroomId);
    }

    public void createTopic(Long chatroomId) {
        createTopic(chatroomId, 1, (short) 1);
    }

}
