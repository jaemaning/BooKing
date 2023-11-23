package com.booking.chat.socket.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StompTest {

    protected StompSession stompSession;

    @LocalServerPort
    private int port;

    private final String url;

    private final WebSocketStompClient webSocketStompClient;

    public StompTest() {
        this.webSocketStompClient = new WebSocketStompClient(new SockJsClient(createTransport()));
        this.webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        this.url = "ws://localhost:";
    }

    @BeforeEach
    public void connect() throws ExecutionException, InterruptedException, TimeoutException {
        this.stompSession = this.webSocketStompClient
            .connect(url + port + "/booking/chat", new StompSessionHandlerAdapter() {})
            .get(3, TimeUnit.SECONDS);
    }

    @AfterEach
    public void disconnect() {
        if (this.stompSession.isConnected()) {
            this.stompSession.disconnect();
        }
    }

    private List<Transport> createTransport() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }
}
