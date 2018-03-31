package com.pokerface.pokerapi.game;

import com.pokerface.pokerapi.users.UserInfoTransport;
import com.pokerface.pokerapi.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The GameController serves vital function as the communication hub of the Front End users and the Back End server.
 * <p>
 * It exists to receive incoming messages and using the classes and methods of GameService, UserService and AIService
 * handle those requests and communication the appropriate message or object.
 * <p>
 * It uses a combination of REST and STOMP end points for communication, REST for user initiated communication and STOMP
 * for unprompted communications.
 */
@RestController
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final GameService gameService;
    private final UserService userService;
    private final AIService aiService;
    private final SimpMessagingTemplate messenger;

    public GameController(final GameService gameService,
                          final UserService userService,
                          final AIService aiService,
                          final TaskScheduler taskScheduler,
                          final SimpMessagingTemplate messenger) {
        this.gameService = gameService;
        this.userService = userService;
        this.aiService = aiService;
        this.messenger = messenger;
    }

    /**
     * receivedManagmentAction receives authetnicated actions from the managing user to modify settings of the game.
     */
    @MessageMapping("/game/{game_id}/control")
    public void receiveManagementAction() {

    }

    @Scheduled(fixedRate = 1000)
    public void checkEvents() {
        List<Long> startedGames = gameService.startingGameIDs();
        for (Long gameID :startedGames) {
            GameStateTransport gameTransport = gameService.gameStart(gameID);
            gameTransport.reason(GameStateTransport.Reason.GAME_STARTED, "the game has started");
            messenger.convertAndSend("/messages/game/"+gameID, gameTransport);
            long[] userIDs=gameService.getUserIDsFromGame(gameID);
            for(long userID: gameService.getUserIDsFromGame(gameID)) {
                UserInfoTransport user = userService.getUser(userID);
                HandTransport userHand = gameService.getHandTransport(gameID, userID);
                messenger.convertAndSendToUser(user.getUsername(), "/messages/game/"+gameID, userHand);
            }
        }
    }
//    @MessageMapping("/game/{game_id}/ready")
//    public void readyUp(@DestinationVariable("game_id") long gameID, Principal principal){
//        UserInfoTransport user = userService.getUserByUsername(principal.getName());
//
//
//        if (gameService.readyUp(user.getId())) {
//            startGame(gameID);
//        }
//    }
//
//    public void startGame(long gameID) {
//        List<HandTransport> hands = gameService.deal(gameID);
//
//        for (HandTransport hand : hands) {
//            UserInfoTransport user = userService.getUser(hand.getUserId());
//            messenger.convertAndSendToUser(hand.getUser(),"/game/" + gameID, hand);
//        }
//    }

    /**
     * Listen for a user to disconnect from the websocket. If a user is participating
     * in any game, they will be removed, and a new GameState will be broadcast to the
     * clients of the game they were participating in.
     *
     * @param sessionEvent the event that occurred
     */
    @EventListener
    public void listenForWebsocketDisconnect(SessionDisconnectEvent sessionEvent) {
        Principal principal = sessionEvent.getUser();
        logger.info("a user disconnected from the websocket: " + principal);

        if (principal == null) {
            return;
        }

        UserInfoTransport user = userService.getUserByUsername(principal.getName());
        List<Long> games = new ArrayList<>(); //gameService.findAllGamesWithUser(user.getId());
        for (Long gameID : games) {
            gameService.removePlayer(gameID, user.getId());
            GameStateTransport newGameState = gameService.getGameStateTransport(gameID);
            messenger.convertAndSend("/messages/game/" + gameID, newGameState.reason(GameStateTransport.Reason.PLAYER_LEFT,
                    user.getUsername() + " has left the game"));
        }

    }

    /**
     * receiveAction tales am action from the user and processes it, ultimately passing it to handleAction which then
     * passed it to its gameService to modify gameState and transport those changes
     *
     * @param action    the action that the user has sent
     * @param gameID    the long gameID the action is being performed on
     * @param principal the user that sent this message
     */
    @MessageMapping("/game/{game_id}")
    public void receiveAction(@Payload GameAction action,
                              @DestinationVariable("game_id") long gameID,
                              Principal principal) {
        UserInfoTransport user = userService.getUserByUsername(principal.getName());
        int playerId = gameService.getPlayerID(gameID, user.getId());

        GameStateTransport nextGameState = handleAction(gameID, action, playerId);

        while (aiService.isAIPlayer(gameID, nextGameState.getNextPlayer())) {
            GameAction aiAction = aiService.playAction(gameID);
            nextGameState = handleAction(gameID, aiAction, nextGameState.getNextPlayer());
        }
    }

    @MessageMapping("/game/{game_id}/gamestate")
    @SendTo("/messages/game/{game_id}")
    public GameStateTransport testGameState(@Payload GameAction action,
                                            @DestinationVariable("game_id") long gameID,
                                            Principal principal) {
        UserInfoTransport user = userService.getUserByUsername(principal.getName());
        UserInfoTransport jason = userService.getUserByUsername("jason");
        UserInfoTransport admin = userService.getUserByUsername("admin");

        GameStateTransport gameState = new GameStateTransport();
        if (user.getId() == jason.getId()) {
            gameState.setNextPlayer(1);
        } else {
            gameState.setNextPlayer(2);
        }
//int id, int money, GameAction action, boolean isPlayer, boolean isDealer,boolean isFold,int amountBet
        gameState.setPlayers(new PlayerTransport[]{
                new PlayerTransport(
                        1,
                        200,
                        new GameAction(GameActionType.BET, 1),
                        true,
                        true,
                        true,
                        0,
                        "Sal"
                ), // admin
                new PlayerTransport(
                        2,
                        10000,
                        new GameAction(GameActionType.BET, 1),
                        true,
                        false,
                        true,
                        0,
                        "Fred"
                )}); // jason
        gameState.setBigBlind(10);
        gameState.setCommunityCards(Arrays.asList(Card.SPADES_QUEEN, Card.SPADES_SEVEN, Card.SPADES_KING));
        gameState.setPotSum(30);

        return gameState.reason(GameStateTransport.Reason.PLAYER_ACTION, "");
    }

//    @SendTo("game/{game_id}/{user_id}")
//    public HandTransport sendHand(@DestinationVariable("game_id")long gameID, @DestinationVariable("user_id")long userID){
//        return gameService.getHandTransport(gameID,userID);
//    }

    @MessageMapping("test")
    public void testWebsocket(@Payload long number, Principal principal) {
        messenger.convertAndSend("/messages/game", new GameInfoTransport(number));
        messenger.convertAndSendToUser(principal.getName(), "/messages/game", new GameInfoTransport(number));
    }

    /**
     * handleAction is what takes a received action and breaks it down to send to gameService. This updates the
     * gameState and returns a transport object for the user to update their Front End
     *
     * @param gameID   the gameID of the game to be modified
     * @param action   the action being performed
     * @param playerID the playerID performing the action
     * @return a GameStateTrasnport, returning the new state of the game for the Front End user
     */
    private GameStateTransport handleAction(long gameID, GameAction action, int playerID) {
        GameStateTransport nextGameState = gameService.handleAction(gameID, action, playerID);
        nextGameState.reason(GameStateTransport.Reason.PLAYER_ACTION,playerID+" played an action.");
        messenger.convertAndSend("/messages/game/" + gameID, nextGameState);

        if (gameService.isHandEnd(gameID)) {
            HandEndTransport winners = gameService.determineWinnings(gameID);
            nextGameState = gameService.getGameStateTransport(gameID);

            messenger.convertAndSend("/messages/game/" + gameID,
                    nextGameState.reason(GameStateTransport.Reason.HAND_FINISHED, ""));
            if (gameService.getGameType(gameID).equals("COMPETITIVE")){
                int[] ratingChanges=gameService.calculateRatingChanges(gameID);
                long[] userIDs=gameService.getUserIDsFromGame(gameID);
                for (int i=0;i<userIDs.length;i++){
                    userService.applyRatingChange(userIDs[i],ratingChanges[i]);
                }
            }
        } else if (gameService.isRoundEnd(gameID)) {
            nextGameState = gameService.handleRound(gameID);
            messenger.convertAndSend("/messages/game/" + gameID,
                    nextGameState.reason(GameStateTransport.Reason.ROUND_FINSHED, ""));
        }

        return nextGameState;
    }

    /**
     * playerLeaveGame takes requests from a player to leave a game, or if they are idle for too long and removes them
     *
     * @param gameID    the long gameID to remove
     * @param principal the user identification
     */
    @DeleteMapping("/api/v1/games/{gameID}/{userID}")
    public ResponseEntity playerLeaveGame(@PathVariable("gameID") long gameID,
                                @PathVariable("userID") long userID,
                                Principal principal) {

        UserInfoTransport user = userService.getUserByUsername(principal.getName());

        if (user.getId() != userID) {
            throw new UserAccessNotPermittedException(user.getUsername(), gameID, "deletion of user " + userID);
        }

        gameService.removePlayer(gameID, user.getId());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    /**
     * casualGameMatchmaking takes requests from the user for a casual matchmaking game and returns the game
     *
     * @param principal the user requesting a game
     * @return a GameInfoTransport containing the game info
     */
    @PostMapping("/api/v1/matchmaking/basicGame")
    public GameInfoTransport casualGameMatchmaking(Principal principal) {
        UserInfoTransport user = userService.getUserByUsername(principal.getName());
        long gameID = gameService.casualMatchmake(user.getId(), user.getUsername());
        GameStateTransport gameStateTransport = gameService.getGameStateTransport(gameID);
        messenger.convertAndSend("/messages/game/" + gameID,
                gameStateTransport.reason(GameStateTransport.Reason.PLAYER_JOINED, "User has joined"));
        return new GameInfoTransport(gameID);
    }


    @PostMapping("/api/v1/matchmaking/competitiveGame")
    public GameInfoTransport competitiveGameMatchmaking(Principal principal) {
        UserInfoTransport user = userService.getUserByUsername(principal.getName());
        long gameID = gameService.competitiveMatchmake(user.getId(), user.getUsername());
        GameStateTransport gameStateTransport = gameService.getGameStateTransport(gameID);
        messenger.convertAndSend("/messages/game/" + gameID,
                gameStateTransport.reason(GameStateTransport.Reason.PLAYER_JOINED, "User has joined"));
        return new GameInfoTransport(gameID);
    }

    @GetMapping("/api/v1/games")
    public void getGameListing() {
        gameService.getGameStateList();
    }

    @PostMapping("/api/v1/games")
    public GameInfoTransport createCustomGame(Principal principal) {

        GameInfoTransport newGame = new GameInfoTransport(gameService.createGame(3, GameState.GameType.CUSTOM));
        gameService.getGameState(newGame.getGameId()).addPlayer(userService.getUserByUsername(principal.getName()).getId(), principal.getName());
        return newGame;
    }

    /**
     * This method getsGameInfo of a specific game and responds with the info the user needs to display it
     *
     * @param gameID a long value representing the gameState in repository
     * @return the GameStateTransport of that game
     */
    @GetMapping("/api/v1/games/{id}")
    public GameStateTransport getGameInfo(@PathVariable("id") long gameID) {
        GameStateTransport game = gameService.getGameStateTransport(gameID);

        if (game == null) throw new GameDoesNotExistException(gameID);

        return game;
    }

    @GetMapping("/api/v1/games/{id}/cards")
    public HandTransport getUserHand(@PathVariable("id") long gameID, Principal principal) {
        UserInfoTransport user = userService.getUserByUsername(principal.getName());

        HandTransport hand = gameService.getHandTransport(gameID, user.getId());

        if (hand == null) {
            throw new UserAccessNotPermittedException(user.getUsername(), gameID, "cards");
        }

        return hand;
    }


//
//    @PutMapping("/api/v1/games/{id}")
//    public void updateGameRules(@PathVariable("id") long gameId) {
//
//    }
//
//    @DeleteMapping("/api/v1/games/{id}")
//    public void deleteOwnedGame(@PathVariable("id") long gameID) {
//        gameService.deleteGame(gameID)
//    }

    @DeleteMapping("/api/v1/games/{id}")
    public ResponseEntity leaveGame(@PathVariable("id") long gameID, Principal principal) {
        if (gameService.removePlayer(gameID, userService.getUserByUsername(principal.getName()).getId())==1){
          closeGame(gameID);
        }
        GameStateTransport gameStateTransport = gameService.getGameStateTransport(gameID);
        gameStateTransport.reason(GameStateTransport.Reason.PLAYER_LEFT, principal.getName() + " has left.");
        messenger.convertAndSend("/messages/games/" + gameID, gameStateTransport);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    public void closeGame(long gameID){
        gameService.removeGame(gameID);
    }


}