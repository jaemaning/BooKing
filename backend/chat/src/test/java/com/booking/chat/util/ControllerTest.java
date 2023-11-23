package com.booking.chat.util;

import com.booking.chat.message.controller.MessageController;
import com.booking.chat.message.repository.MessageRepository;
import com.booking.chat.message.service.MessageService;
import com.booking.chat.chatroom.controller.ChatroomController;
import com.booking.chat.chatroom.repository.ChatroomRepository;
import com.booking.chat.chatroom.service.ChatroomService;
import com.booking.chat.global.jwt.JwtUtil;
import com.booking.chat.kafka.domain.KafkaMessage;
import com.booking.chat.kafka.service.ChatListenerService;
import com.booking.chat.mongo.service.SequenceGeneratorService;
import com.booking.chat.mongo.util.MessageIncrementListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureRestDocs
@ActiveProfiles("local")
@ExtendWith(RestDocumentationExtension.class)
@WebFluxTest({
    ChatroomController.class,
    MessageController.class
})
public class ControllerTest {

    //setup
    // @Autowired private WebApplicationContext webApplicationContext;
    @Autowired protected RestDocumentationContextProvider restDocumentation;

    @Autowired protected WebTestClient webTestClient;
    @Autowired protected ObjectMapper objectMapper;

    //Service
    @MockBean protected ChatListenerService chatListenerService;
    @MockBean protected SequenceGeneratorService sequenceGeneratorService;
    @MockBean protected MessageIncrementListener messageIncrementListener;
    @MockBean protected MessageService messageService;
    @MockBean protected ChatroomService chatroomService;

    //Repository
    @MockBean protected MessageRepository messageRepository;
    @MockBean protected ChatroomRepository chatroomRepository;

    //util
    @MockBean protected KafkaTemplate<String, KafkaMessage> kafkaTemplate;
    @MockBean private MappingMongoConverter mappingMongoConverter;
    @MockBean protected JwtUtil jwtUtil;


}
