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

    public StompSession getStompSession(String username, String password) throws InterruptedException, ExecutionException, TimeoutException, KeyManagementException, NoSuchAlgorithmException {

        StandardWebSocketClient socket = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(socket);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());


        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        if (username != null) {
            restTemplate = restTemplate.withBasicAuth(username, password);

            ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/users/login", String.class);
            String cookies = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
            headers.add(HttpHeaders.COOKIE, cookies.split(";")[0]);
        }

        return stompClient.connect(this.URL, headers, new StompSessionHandlerAdapter() {
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


    @Test
    public void testTestEndpoint() throws Exception {
        StompSession session = getStompSession("admin", "admin");
        future = new CompletableFuture<GameInfoTransport>();
        session.subscribe("/messages/game", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return GameInfoTransport.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                future.complete((GameInfoTransport) payload);
            }
        });

        session.send("/app/test", 1);
        Assert.assertEquals(1, ((GameInfoTransport) future.get()).getGameId());
    }
}
