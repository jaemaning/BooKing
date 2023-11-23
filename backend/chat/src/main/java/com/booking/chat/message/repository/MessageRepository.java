package com.booking.chat.message.repository;

import com.booking.chat.message.domain.Message;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface MessageRepository extends ReactiveMongoRepository<Message, Long> {

    @Tailable
    @Query("{ chatroomId:  ?0}")
    Flux<Message> findByChatRoomId(Long roomId);

    Flux<Message> findByChatroomIdAndMessageIdGreaterThanEqual(Long chatroomId, Long lastMessageId);

}
