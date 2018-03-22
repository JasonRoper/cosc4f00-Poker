package com.pokerface.pokerapi.game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ControllerTest {

    @Value("${local.server.port}")
    private int port;
    private String URL;


    @Autowired
    private MockMvc mvc;

    @Before
    public void setup(){
        this.URL = "wss://localhost:" + port + "/live";
    }

    public StompSession getStompSession() throws InterruptedException, ExecutionException, TimeoutException, KeyManagementException, NoSuchAlgorithmException {

        StandardWebSocketClient socket = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(socket);

        return stompClient.connect(this.URL, new StompSessionHandlerAdapter() {
        }).get();
    }

    @Test
    @WithUserDetails("admin")
    public void testMatchmaking() throws Exception {

        mvc.perform(post("/api/v1/matchmaking/basicGame"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").isNumber());
    }

    @Test
    @WithUserDetails("admin")
    public void testTestEndpoint() throws Exception {
        StompSession session = getStompSession();
        session.subscribe("/messages/game", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Assert.assertEquals("admin is talking to me", (String) payload);
            }
        });
        session.send("/app/test", 1);
    }
}
