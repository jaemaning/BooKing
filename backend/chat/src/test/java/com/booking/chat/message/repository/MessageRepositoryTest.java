package com.booking.chat.message.repository;

import com.booking.chat.message.domain.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataMongoTest
class MessageRepositoryTest {

    @Autowired
    MessageRepository messageRepository;

    @BeforeEach
    void tearDown() throws Exception {
        messageRepository.deleteAll();
    }

    @Test
    @DisplayName("MongoDB 연동 테스트")
    void t1() throws Exception {

        Message testMessage = Message.builder()
                                     ._id("123")
                                     .memberId(1L)
                                     .chatroomId(1L)
                                     .content("하이")
                                     .build();

//        messageRepository.save(testMessage);
//
//        Flux<Message> messageList = messageRepository.findAll();
//
//        assertThat(messageListsize()).isEqualTo(1L);
    }

}