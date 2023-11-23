package com.booking.chat.mongo.util;

import com.booking.chat.message.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageIncrementListener extends AbstractMongoEventListener<Message> {

//    private final SequenceGeneratorService sequenceGeneratorService;
//    private final ChatroomService chatroomService;
//
//    @Override
//    public void onBeforeConvert(BeforeConvertEvent<Message> event) {
//        long chatroomId = event.getSource().getChatroomId();
//        chatroomService.findByChatroomId(chatroomId)
//            .publishOn(Schedulers.boundedElastic())
//            .doOnNext(chatroom -> {
//                chatroom.updateIndex();
//                chatroomService.save(chatroom).subscribe();
//            }).subscribe();
    }

//    @Override
//    public void onAfterSave(AfterSaveEvent<Message> event) {
//        long chatroomId = event.getSource().getChatroomId();
//        chatroomService.findByChatroomId(chatroomId)
//                       .publishOn(Schedulers.boundedElastic())
//                       .doOnNext(chatroom -> {
//            chatroom.updateListMessageReceived();
//            chatroom.updateLastMessage(event.getSource().getContent());
//            chatroomService.save(chatroom).subscribe();
//        }).subscribe();
//    }
//}
