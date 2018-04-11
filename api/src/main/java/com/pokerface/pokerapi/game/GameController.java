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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.ArrayList;
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
    private boolean ping=true;


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

//    @Scheduled(fixedDelay = 10000)
//    public void checkUp(){
//        if (ping){
//            ping=false;
//        } else {
//            logger.debug("checkedEventsStopped?");
//        }
//    }

    @Scheduled(fixedDelay = 1000)
    public void checkEvents() {
        try {
            ping=true;
            List<Long> startedGames = gameService.startingGameIDs();
            for (Long gameID : startedGames) {
                GameStateTransport gameTransport = gameService.gameStart(gameID);
                gameTransport.reason(GameStateTransport.Reason.GAME_STARTED, "the game has started");
                messenger.convertAndSend("/messages/game/" + gameID, gameTransport);
                long[] userIDs = gameService.getUserIDsFromGame(gameID);
                for (long userID : userIDs) {
                    if (userID < 1000000) {
                        UserInfoTransport user = userService.getUser(userID);
                        HandTransport userHand = gameService.getHandTransport(gameID, userID);
                        messenger.convertAndSendToUser(user.getUsername(), "/messages/game/" + gameID, userHand);
                    }
                }
//            GameStateTransport nextGameState=gameService.getGameStateTransport(gameID);
//            while (gameService.isAITurn(gameID)) {
//                GameAction aiAction = aiService.playAction(gameService,gameID);
//                nextGameState = handleAction(gameID, aiAction, nextGameState.getNextPlayer());
//            }
            }
        } catch (Exception e) {
            logger.debug("Check Events Broken:"+e.getStackTrace().toString());
        }
    }

    @Scheduled(fixedDelay=5000)
    public void AILivelinessFix(){
        List<Long> gamesToCheck=gameService.getPotentialAIGames();
        Iterable gamesTest = gameService.getGameList();
        for (Long gameID:gamesToCheck){
            GameStateTransport nextGameState=gameService.getGameStateTransport(gameID);
                GameAction aiAction = aiService.playAction(gameService,gameID);
                handleAction(gameID, aiAction, nextGameState.getNextPlayer());
        }
    }

//    @Scheduled(fixedRate=30000)
//    public void cleanUpDatabase(){
//        gameService.clearIdleGames();
//    }

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
        GameState gameState = gameService.getGameState(gameID);
        GameStateTransport nextGameState = handleAction(gameID, action, playerId);

//        while (gameService.isAITurn(gameID)) {
//            GameAction aiAction = aiService.playAction(gameService,gameID);
//            nextGameState = handleAction(gameID, aiAction, nextGameState.getNextPlayer());
//        }
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
        nextGameState.reason(GameStateTransport.Reason.PLAYER_ACTION,nextGameState.getPlayers()[playerID].getName()+" performed a "+action.getType());

        messenger.convertAndSend("/messages/game/" + gameID, nextGameState);


        if (gameService.isHandEnd(gameID)) {
            HandEndTransport winners = gameService.determineWinnings(gameID);
            nextGameState = gameService.getGameStateTransport(gameID);

            messenger.convertAndSend("/messages/game/" + gameID, nextGameState.reason(GameStateTransport.Reason.HAND_FINISHED, ""));
            if (gameService.getGameType(gameID).equals("COMPETITIVE")){
                int[] ratingChanges=gameService.calculateRatingChanges(gameID,winners);
                long[] userIDs=gameService.getUserIDsFromGame(gameID);
                for (int i=0;i<userIDs.length;i++){
                    userService.applyRatingChange(userIDs[i],ratingChanges[i]);
                }
            }
            messenger.convertAndSend("/messages/game/" + gameID+"/gamefinished",winners);
            if (gameService.isGameEnd(gameID)){
                messenger.convertAndSend("/messages/game/" + gameID, nextGameState.reason(GameStateTransport.Reason.GAME_FINISHED, ""));
                closeGame(gameID);
            }
        } else if (gameService.isRoundEnd(gameID)) {
            nextGameState = gameService.handleRound(gameID);
            messenger.convertAndSend("/messages/game/" + gameID,
                    nextGameState.reason(GameStateTransport.Reason.ROUND_FINISHED, ""));
        }

            return nextGameState;
    }




    /**
     * casualGameMatchmaking takes requests from the user for a casual matchmaking game and returns the game
     *
     * @param principal the user requesting a game
     * @return a GameInfoTransport containing the game info
     */
    @PostMapping("/api/v1/matchmaking/casualGame")
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

    @PostMapping("/api/v1/games/")
    public GameInfoTransport createCustomGame(@RequestBody GameSettingTransport gameSettings,
                                              Principal principal) {
        UserInfoTransport user = userService.getUserByUsername(principal.getName());
        long gameID;
        if (gameSettings.gameType == GameState.GameType.AI || gameSettings.gameType==null) {
            gameID = gameService.createAIGame(gameSettings, user.getId(), user.getUsername());
        } else {
            gameID = gameService.createGame(gameSettings, user.getId(), user.getUsername());
        }
        GameStateTransport gameStateTransport = gameService.getGameStateTransport(gameID);
        return new GameInfoTransport(gameID);
    }

    @GetMapping("/api/v1/games")
    public void getGameListing() {
        gameService.getGameStateList();
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

    /**
     * playerLeaveGame takes requests from a player to leave a game, or if they are idle for too long and removes them
     *
     * @param gameID    the long gameID to remove
     * @param principal the user identification
     */
    @DeleteMapping("/api/v1/games/{gameID}/{userID}")
    public ResponseEntity leaveGame(@PathVariable("gameID") long gameID,
                                          @PathVariable("userID") long userID,
                                          Principal principal) {

        UserInfoTransport user = userService.getUserByUsername(principal.getName());

        if (user.getId() != userID) {
            throw new UserAccessNotPermittedException(user.getUsername(), gameID, "deletion of user " + userID);
        }
        if (gameService.doesGameExist(gameID)) {
            GameStateTransport gameStateTransport = gameService.getGameStateTransport(gameID);
            if (gameService.removePlayer(gameID, userService.getUserByUsername(principal.getName()).getId()) == 1) {
                closeGame(gameID);
            } else {

                gameStateTransport.reason(GameStateTransport.Reason.PLAYER_LEFT, principal.getName() + " has left.");
                messenger.convertAndSend("/messages/games/" + gameID, gameStateTransport);
            }
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    public void closeGame(long gameID){
        gameService.removeGame(gameID);
    }


}