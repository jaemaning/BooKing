package com.booking.chat.mongo.util;

import com.booking.chat.chatroom.domain.Chatroom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChatroomModifiedTimeListener extends AbstractMongoEventListener<Chatroom> {


}
