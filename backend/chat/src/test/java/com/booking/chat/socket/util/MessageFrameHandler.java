package com.booking.chat.socket.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

public class MessageFrameHandler<T> implements StompFrameHandler {

    @Getter
    private final List<T> receivedMessages = new ArrayList<>();

    @Getter
    private final CompletableFuture<T> completableFuture = new CompletableFuture<>();

    private final Class<T> tClass;

    public MessageFrameHandler(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return this.tClass;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        receivedMessages.add((T) payload);
        completableFuture.complete((T)payload);
    }


}
