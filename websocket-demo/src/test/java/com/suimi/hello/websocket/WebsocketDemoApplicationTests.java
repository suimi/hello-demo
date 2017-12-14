package com.suimi.hello.websocket;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebsocketDemoApplicationTests {

    @LocalServerPort
    private int port;

    private List<Transport> transports = new ArrayList<>();


    private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

    @Before
    public void setup() {
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
    }

    @Test
    public void getGreeting() throws Exception {
        int totalSize = 2;
        final CountDownLatch latch = new CountDownLatch(totalSize);
        final AtomicReference<Throwable> failure = new AtomicReference<>();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (int i = 0; i < totalSize; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    SockJsClient sockJsClient = new SockJsClient(transports);
                    WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
                    stompClient.setMessageConverter(new MappingJackson2MessageConverter());
                    stompClient.connect("ws://localhost:{port}/websocket", this.headers,
                            new TestSessionHandler(failure, finalI), this.port);
                } catch (Throwable t) {
                    log.error("connection {} eror", finalI);
                    failure.set(t);
                    latch.countDown();
                }
            });
        }
        if (latch.await(180, TimeUnit.SECONDS)) {
            if (failure.get() != null) {
                throw new AssertionError("", failure.get());
            }
        } else {
            fail("Greeting not received");
        }

    }

    private class TestSessionHandler extends StompSessionHandlerAdapter {

        private final AtomicReference<Throwable> failure;

        private int index;


        public TestSessionHandler(AtomicReference<Throwable> failure, int index) {
            this.failure = failure;
            this.index = index;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            this.failure.set(new Exception(headers.toString()));
        }

        @Override
        public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
            this.failure.set(ex);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable ex) {
            this.failure.set(ex);
        }

        @Override
        public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
            StompFrameHandler stompFrameHandler = new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return Greeting.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    Greeting greeting = (Greeting) payload;
                    try {
                        if (log.isDebugEnabled()) {
                            log.debug("session:{} received message {}", session.getSessionId(), greeting);
                        }
                        //                            assertEquals("Hello, web socket!", greeting.getContent());
                    } catch (Throwable t) {
                        failure.set(t);
                    } finally {
//                        session.disconnect();
                    }
                }
            };
            session.subscribe("/topic/greetings", stompFrameHandler);
            session.subscribe("/user/" + index + "/oneToOne", stompFrameHandler);
            try {
                Thread.sleep(1000);
                session.send("/app/hello", new Message(index, "web socket"));
                session.send("/app/oneToOne", new Message(index, "One to one message"));
            } catch (Throwable t) {
                failure.set(t);
            }
        }
    }
}
