package com.pokerface.pokerapi.game;

import com.pokerface.pokerapi.users.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class TestGame {
    @Value("${local.server.port}")
    private int port;
    private String URL;


    @Autowired
    private TestRestTemplate restTemplate;


    @Before
    public void setup() {
        this.URL = "ws://localhost:" + port + "/live";
    }

    @Test
    public void testTestEndpoint() throws Exception {
        WebsocketSession socket = new WebsocketSession("admin", "admin");
        CompletableFuture<GameInfoTransport> future = socket.subscribe("/messages/game", GameInfoTransport.class);
        socket.send("/app/test", 1);

        assertEquals(1, (future.get()).getGameId());
    }

    @Test
    public void testMatchmakingWhenNoGameExists() throws ExecutionException, InterruptedException, TimeoutException {
        TestRestTemplate adminRest = restTemplate.withBasicAuth("admin", "admin");

        ResponseEntity<GameInfoTransport> matchmakingResponse = adminRest.postForEntity("/api/v1/matchmaking/basicGame", null, GameInfoTransport.class);
        assertEquals(matchmakingResponse.getStatusCode(), HttpStatus.OK);

        WebsocketSession adminWebsocket = new WebsocketSession("admin", "admin");

        CompletableFuture<GameStateTransport> future = adminWebsocket.subscribe(
                "/messages/game/" + matchmakingResponse.getBody().getGameId(),
                GameStateTransport.class);
        GameStateTransport nextGameState = future.get(1000, TimeUnit.SECONDS);
    }

    @Test
    public void testMatchmakingWhenGameExists(){}

    @Test
    public void testMatchmakingWhenAnotherGameIsStarted() {}

    @Test
    public void testGameStartsAfter3PlayersJoined() {}

    @Test
    public void testGameRefusesActionsUnlessGameStarted() {}

    @Test
    public void testGameRefusesActionsOfAllButCurrentPlayer() {}

    @Test
    public void testGameFold() {}

    @Test
    public void testGameBet() {}

    @Test
    public void testGameCheck() {}

    @Test
    public void testInvalidAction() {}

    @Test
    public void testRoundEnd() {}

    @Test
    public void testAllButOneFold() {}

    @Test
    public void testShowdownTie() {}

    @Test
    public void testShowdownWinner() {}

    @Test
    public void testPlayerDisconnectWhenGameShouldStart() {}

    @Test
    public void testPlayerDisconnectAndReconnectDuringGame() {}

    @Test
    public void testPlayerDisconnectAndTimeOutDuringGame() {}

    public GameState createTestGameState(GameStage stage) {
        GameState result = new GameState();
        return result;
    }

    public enum GameStage {
        WAITING, STARTING, STARTED, OVER
    }


    public static class MyStompFrameHandler<T> implements StompFrameHandler {
        private Type type;
        private CompletableFuture<T> future;

        public MyStompFrameHandler(CompletableFuture<T> future, Type type) {
            this.type = type;
            this.future = future;
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return type;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            future.complete((T) payload);
        }
    }

    public class WebsocketSession {

        private Map<String, CompletableFuture> futures;
        private StompSession session;
        private String authCookie;
        private String path;

        /**
         * Create an unauthenticated websocket connection
         */
        public WebsocketSession() throws ExecutionException, InterruptedException {
            this(null, null);
        }

        public WebsocketSession(String username, String password) throws ExecutionException, InterruptedException {
            this.path = URL;
            this.futures = new TreeMap<>();

            if (username != null && password != null) {
                TestRestTemplate restTemplate = TestGame.this.restTemplate.withBasicAuth(username, password);
                ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/users/login", String.class);
                authCookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE).split(";")[0];
            }

            this.connect();
        }

        public void connect() throws ExecutionException, InterruptedException {
            if (this.session != null && this.session.isConnected()){
                return;
            }

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
                    System.out.println("Exception occurred on command: " + command + " with headers: " + headers + " on payload: " + str + " with exception: " + exception);

                    for (CompletableFuture future : futures.values()) {
                        future.cancel(true);
                    }

                }
            }).get();
        }

        public StompSession getSession() {
            return session;
        }

        public<T> CompletableFuture<T> subscribe(String path, Class<T> type) {
            CompletableFuture<T> future = new CompletableFuture<>();
            this.futures.put(path, future);
            session.subscribe(path, new MyStompFrameHandler<>(future, type));
            return future;
        }

        public void send(String path, Object payload) {
            session.send(path, payload);
        }

        public void disconnect() {
            session.disconnect();
        }
    }

}
