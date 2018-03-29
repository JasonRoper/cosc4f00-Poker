package com.pokerface.pokerapi.game;

import com.pokerface.pokerapi.users.EmailAlreadyExistsException;
import com.pokerface.pokerapi.users.RegistrationFields;
import com.pokerface.pokerapi.users.UserInfoTransport;
import com.pokerface.pokerapi.users.UserService;
import org.junit.After;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class GameTest {
    @Value("${local.server.port}")
    private int port;
    private String URL;
    private List<Long> userIDs=new ArrayList();
    private final String gameStateMessagePath = "/messages/game/"; // Gamestates+gameID
    private final String handMessagePath = "/user/messages/game/"; // This is for hands+gameID

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        this.URL = "ws://localhost:" + port + "/live";
    }

    @After
    public void cleanUp(){
        cleanUpUserRepository();
        cleanUpGameRepository();
    }

    @Test
    public void testUserRepositorySetUp(){
        setUpUserRepository();
        List<UserInfoTransport>listOfUsers=userService.listUsers();
        cleanUpUserRepository();
        System.out.println();
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

        ResponseEntity<GameStateTransport> gameStateResponse = adminRest.getForEntity("/api/v1/games/"+matchmakingResponse.getBody().getGameId(),GameStateTransport.class);
        assertEquals(gameStateResponse.getStatusCode(),HttpStatus.OK);

        GameState gameState = gameRepository.findOne(matchmakingResponse.getBody().getGameId());
        assertEquals(gameStateResponse.getBody(),new GameStateTransport(gameState));

    }

    @Test
    public void testMatchmakingWhenGameExists() throws ExecutionException, InterruptedException, TimeoutException{
        TestRestTemplate adminRest = restTemplate.withBasicAuth("admin", "admin");

        ResponseEntity<GameInfoTransport> matchmakingResponse = adminRest.postForEntity("/api/v1/matchmaking/basicGame", null, GameInfoTransport.class);
        assertEquals(matchmakingResponse.getStatusCode(), HttpStatus.OK);

        ResponseEntity<GameStateTransport> gameStateResponse = adminRest.getForEntity("/api/v1/games/"+matchmakingResponse.getBody().getGameId(),GameStateTransport.class);
        assertEquals(gameStateResponse.getStatusCode(),HttpStatus.OK);

        GameState gameState = gameRepository.findOne(matchmakingResponse.getBody().getGameId());
        assertEquals(gameStateResponse.getBody(),new GameStateTransport(gameState)); // First join

        TestRestTemplate firstJoinRest = restTemplate.withBasicAuth("adam", "password");

        matchmakingResponse = firstJoinRest.postForEntity("/api/v1/matchmaking/basicGame", null, GameInfoTransport.class);
        assertEquals(matchmakingResponse.getStatusCode(), HttpStatus.OK);

        gameStateResponse = firstJoinRest.getForEntity("/api/v1/games/"+matchmakingResponse.getBody().getGameId(),GameStateTransport.class);
        assertEquals(gameStateResponse.getStatusCode(),HttpStatus.OK);

        gameState = gameRepository.findOne(matchmakingResponse.getBody().getGameId());
        assertEquals(gameStateResponse.getBody(),new GameStateTransport(gameState));
    }

    @Test
    public void testMatchmakingWhenAnotherGameIsStarted() {
        setUpUserRepository();
        GameState testGameState = createTestGameState(GameStage.STARTED, GameState.GameType.CASUAL);
        TestRestTemplate adminRest = restTemplate.withBasicAuth("admin", "admin");

        ResponseEntity<GameInfoTransport> matchmakingResponse = adminRest.postForEntity("/api/v1/matchmaking/basicGame", null, GameInfoTransport.class);
        assertEquals(matchmakingResponse.getStatusCode(), HttpStatus.OK);

        ResponseEntity<GameStateTransport> gameStateResponse = adminRest.getForEntity("/api/v1/games/"+matchmakingResponse.getBody().getGameId(),GameStateTransport.class);
        assertEquals(gameStateResponse.getStatusCode(),HttpStatus.OK);

        GameState gameState = gameService.getGameState(matchmakingResponse.getBody().getGameId());
        assertEquals(gameStateResponse.getBody(),new GameStateTransport(gameState)); // First join
        assertEquals(gameState,testGameState);
        Iterable<GameState>gameStates=gameRepository.findAll();
        cleanUpUserRepository();
        cleanUpGameRepository();
    }

    @Test
    public void testGameStartsAfter3PlayersJoined() throws Exception {
        setUpUserRepository();

        List<WebsocketSession> webSockets = new ArrayList<>();

        GameState testGameState=createTestGameState(GameStage.WAITING, GameState.GameType.CASUAL);
        long gameID=testGameState.getId();
        TestRestTemplate adminRest = restTemplate.withBasicAuth("admin", "admin");
        webSockets.add(new WebsocketSession("admin","admin"));

        CompletableFuture<HandTransport> gameStartingHandCheck = webSockets.get(0).subscribe("/user/messages/game/" + gameID, HandTransport.class);

        ResponseEntity<GameInfoTransport> matchmakingResponse = adminRest.postForEntity("/api/v1/matchmaking/basicGame", null, GameInfoTransport.class);
        assertEquals(matchmakingResponse.getStatusCode(), HttpStatus.OK);

        ResponseEntity<GameStateTransport> gameStateResponse = adminRest.getForEntity("/api/v1/games/"+matchmakingResponse.getBody().getGameId(),GameStateTransport.class);
        assertEquals(gameStateResponse.getStatusCode(),HttpStatus.OK);

        Iterable<GameState>gameStates=gameRepository.findAll();

        GameState gameState = gameRepository.findOne(matchmakingResponse.getBody().getGameId());
        gameState = gameRepository.findOne(matchmakingResponse.getBody().getGameId());
        GameStateTransport testGameStateResponse = new GameStateTransport((gameState));
        // assertEquals(gameStateResponse.getBody(),testGameStateResponse); // First join


        CompletableFuture<GameStateTransport> gameStartedCheck = webSockets.get(0).subscribe("/messages/game/" + testGameState.getId(), GameStateTransport.class);
        GameStateTransport testGameStateTransport = gameStartedCheck.get(300, TimeUnit.SECONDS);
        if (testGameStateTransport.getEvent().getAction() == GameStateTransport.Reason.PLAYER_JOINED) {
            gameStartedCheck = webSockets.get(0).subscribe("/messages/game/" + testGameState.getId(), GameStateTransport.class);
            testGameStateTransport = gameStartedCheck.get(300, TimeUnit.SECONDS);
        }


        HandTransport testHandTransport = gameStartingHandCheck.get(300, TimeUnit.SECONDS);


        gameState = gameRepository.findOne(gameID);
        assertTrue(gameState.isHasStarted());
        assertTrue(gameState.getPlayerCount()==4);
        cleanUpUserRepository();
        cleanUpGameRepository();
    }

    @Test
    public void testGameRefusesActionsUnlessGameStarted() throws Exception {
        setUpUserRepository();
        GameState testGameState = createTestGameState(GameStage.WAITING, GameState.GameType.CASUAL);

        List<WebsocketSession> webSockets = new ArrayList<>();

        for (Player p: testGameState.getPlayers()){
            webSockets.add(new WebsocketSession(p.getName(),"Password"));
        }
        List<CompletableFuture<GameStateTransport>> testGameStateTransport = new ArrayList<>();
        for(WebsocketSession webSocket: webSockets){
            testGameStateTransport.add(webSocket.subscribe(gameStateMessagePath+testGameState.getId(),GameStateTransport.class));

            webSocket.send("/app/game/"+testGameState.getId(),new GameAction(GameActionType.FOLD,10));
        }

        testGameState=gameRepository.findOne(testGameState.getId());
        int counter=0;
        for(CompletableFuture<GameStateTransport> testTransport: testGameStateTransport){
            GameStateTransport test=testTransport.get(10,TimeUnit.SECONDS);
            assertTrue(!test.getPlayers()[counter].isFold());
            counter++;
        }
        cleanUpUserRepository();
        cleanUpGameRepository();
    }

    @Test
    public void testGameRefusesActionsOfAllButCurrentPlayer() throws Exception {
        setUpUserRepository();

        GameState testGameState = createTestGameState(GameStage.STARTED, GameState.GameType.CASUAL);

        List<WebsocketSession> webSockets = new ArrayList<>();

        for (Player p: testGameState.getPlayers()){
            webSockets.add(new WebsocketSession(p.getName(),"Password"));
        }
        List<CompletableFuture<GameStateTransport>> testGameStateTransport = new ArrayList<>();
        for(WebsocketSession webSocket: webSockets){
            testGameStateTransport.add(webSocket.subscribe(gameStateMessagePath+testGameState.getId(),GameStateTransport.class));

            webSocket.send("/app/game/"+testGameState.getId(),new GameAction(GameActionType.FOLD,10));
        }

        testGameState=gameRepository.findOne(testGameState.getId());
        int counter=0;
        for(CompletableFuture<GameStateTransport> testTransport: testGameStateTransport){
            GameStateTransport test=testTransport.get(10,TimeUnit.SECONDS);
            if (counter!=3) {
                assertTrue(!test.getPlayers()[counter].isFold());
            } else {
                assertTrue(test.getPlayers()[counter].isFold());
            }
            counter++;
        }

        cleanUpUserRepository();
        cleanUpGameRepository();
    }

    @Test
    public void testGameFold() {
        setUpUserRepository();

        cleanUpUserRepository();
        cleanUpGameRepository();
    }

    @Test
    public void testGameBet() {
        setUpUserRepository();

        cleanUpUserRepository();
        cleanUpGameRepository();
    }

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

    public GameState createTestGameState(GameStage stage,GameState.GameType gameType) {
        GameState result = new GameState();
        UserInfoTransport user;
        if (stage==GameStage.CREATED){
            user=userService.getUser(userIDs.get(0));
            userIDs.remove(0);
            result.setGameType(gameType);
            result.addPlayer(user.getId(),user.getUsername());
        } else if (stage==GameStage.STARTED){
            for (int i=0;i<4;i++){
                user=userService.getUser(userIDs.get(0));
                result.addPlayer(user.getId(),user.getUsername());
                userIDs.remove(0);
            }
            result.startGame();
        } else if (stage==GameStage.WAITING){
            for (int i=0;i<3;i++){
                user=userService.getUser(userIDs.get(0));
                result.addPlayer(user.getId(),user.getUsername());
                userIDs.remove(0);
            }
        }
        result.setGameType(gameType);
        gameRepository.save(result);
        return result;
    }

    public enum GameStage {
        CREATED, WAITING, STARTING, STARTED, OVER
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
                TestRestTemplate restTemplate = GameTest.this.restTemplate.withBasicAuth(username, password);
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

    public void setUpUserRepository(){
        char nameLetter='a';
        for (int i=0;i<25;i++){
            String name="ab"+nameLetter;
            nameLetter++;
            try {
                userIDs.add(userService.register(new RegistrationFields(name, "Password", nameLetter + "@gmail.com")).getId());
            } catch(EmailAlreadyExistsException email){

            }
        }
    }

    public void cleanUpUserRepository(){
        for (Long ID:userIDs) {
            userService.deleteUser(ID);
        }
        userIDs=new ArrayList();
    }

    private void cleanUpGameRepository(){
        gameRepository.deleteAll();
    }

}
