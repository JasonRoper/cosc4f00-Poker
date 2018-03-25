package com.pokerface.pokerapi.game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class TestWebsocket {
    @Value("${local.server.port}")
    private int port;
    protected String URL;


    @Autowired
    private TestRestTemplate restTemplate;


    CompletableFuture future;


    @Before
    public void setup() {
        this.URL = "ws://localhost:" + port + "/live";
    }

    @Test
    public void testTestEndpoint() throws Exception {
        WebsocketSession socket = new WebsocketSession(this.URL, "admin", "admin");
        CompletableFuture future = socket.subscribe("/messages/game", GameInfoTransport.class);
        socket.send("/app/test", 1);
        
        Assert.assertEquals(1, ((GameInfoTransport) future.get()).getGameId());
    }

    public class MyStompFrameHandler implements StompFrameHandler {
        private Type type;
        private CompletableFuture future;

        public MyStompFrameHandler(CompletableFuture future, Type type) {
            this.type = type;
            this.future = future;
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return type;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            future.complete(payload);
        }
    }

    public class WebsocketSession {

        private CompletableFuture future;
        private StompSession session;

        /**
         * Create an unauthenticated websocket connection
         */
        public WebsocketSession(String path) throws ExecutionException, InterruptedException {
            this.connect(path, null);
        }

        public WebsocketSession(String path, String username, String password) throws ExecutionException, InterruptedException {
            TestRestTemplate restTemplate = TestWebsocket.this.restTemplate.withBasicAuth(username, password);
            ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/users/login", String.class);
            String authCookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE).split(";")[0];
            this.connect(path, authCookie);
        }

        private void connect(String path, String authCookie) throws ExecutionException, InterruptedException {
            StandardWebSocketClient socket = new StandardWebSocketClient();

            WebSocketStompClient stompClient = new WebSocketStompClient(socket);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());


            WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
            if (authCookie!= null) {
                headers.add(HttpHeaders.COOKIE, authCookie);
            }


            this.session = stompClient.connect(path, headers, new StompSessionHandlerAdapter() {
                @Override
                public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                    String str = null;
                    try {
                        str = new String(payload, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        System.out.println("cannot parse payload, invalid UTF-8");
                    }
                    System.out.println("Exception occured on command: " + command + " with headers: " + headers + " on payload: " + str + " with exception: " + exception);
                    if (future != null) {
                        future.cancel(true);
                    }

                }
            }).get();
        }

        public CompletableFuture makeFuture(Type type) {
            this.future = new CompletableFuture<Type>();
            return this.future;
        }

        public StompSession getSession() {
            return session;
        }

        public CompletableFuture subscribe(String path, Type type) {
            CompletableFuture future = makeFuture(type);
            session.subscribe(path, new MyStompFrameHandler(future, type));
            return future;
        }

        public void send(String path, Object payload) {
            session.send(path, payload);
        }
    }

}
