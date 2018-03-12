package com.pokerface.pokerapi.game;

import com.pokerface.pokerapi.users.UserInfoTransport;
import com.pokerface.pokerapi.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;

/**
 * The GameController serves vital function as the communication hub of the Front End users and the Back End server.
 *
 * It exists to receive incoming messages and using the classes and methods of GameService, UserService and AIService
 * handle those requests and communication the appropriate message or object.
 *
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


    /**
     * receiveAction tales am action from the user and processes it, ultimately passing it to handleAction which then
     * passed it to its gameService to modify gameState and transport those changes
     * @param action the action being performed
     * @param gameID the long gameID the action is being performed on
     * @param principal the principal authenticated user performing the action
     */
    @MessageMapping("/game/{game_id}")
    public void receiveAction(GameAction action, @DestinationVariable("game_id") long gameID, Principal principal) {

        UserInfoTransport user = userService.getUser(principal.getName());
        int playerId = gameService.getPlayerID(gameID, user.getId());

        GameStateTransport nextGameState = handleAction(gameID, action, playerId);

        while (aiService.isAIPlayer(gameID, nextGameState.nextPlayer())) {
            GameAction aiAction = aiService.playAction(gameID);
            nextGameState = handleAction(gameID, aiAction, nextGameState.nextPlayer());
        }
    }

    /**
     * handleAction is what takes a received action and breaks it down to send to gameService. This updates the
     * gameState and returns a transport object for the user to update their Front End
     * @param gameID the gameID of the game to be modified
     * @param action the action being performed
     * @param playerID the playerID performing the action
     * @return
     */
    private GameStateTransport handleAction(long gameID, GameAction action, int playerID) {
        GameStateTransport nextGameState = gameService.handleAction(gameID, action, playerID);
        messenger.convertAndSend("/messages/game/" + gameID, nextGameState);

        if (gameService.isHandEnd(gameID)) {
            HandEndTransport winners = gameService.determineWinnings(gameID);
            nextGameState = gameService.getGameState(gameID);
            messenger.convertAndSend("/messages/game/" + gameID,
                    nextGameState.reason(GameStateTransport.Reason.HAND_FINISHED,""));
        } else if (gameService.isRoundEnd(gameID)){
            nextGameState = gameService.handleRound(gameID);
            messenger.convertAndSend("/messages/game/" + gameID,
                    nextGameState.reason(GameStateTransport.Reason.ROUND_FINSHED,""));
        }

        return nextGameState;
    }

    /**
     * playerLeaveGame takes requests from a player to leave a game, or if they are idle for too long and removes them
     * @param gameID the long gameID to remove
     * @param principal the user identification
     */
    @DeleteMapping("api/v1/game/{gameID}/")
    public void playerLeaveGame(@DestinationVariable("game_id") long gameID, Principal principal){
        UserInfoTransport user=userService.getUser(principal.getName());
        gameService.playerLeaveGame(gameID,user.getId());
    }

    /**
     * casualGameMatchmaking takes requests from the user for a casual matchmaking game and returns the game
     * @param principal the user requesting a game
     * @return a GameInfoTransport containing the game info
     */
   @PostMapping("/api/v1/matchmaking/basicGame")
   public GameInfoTransport casualGameMatchmaking(Principal principal) {
       UserInfoTransport user = userService.getUser(principal.getName());
       long gameId = gameService.matchmake(user.getId());
        return new GameInfoTransport(gameId);
   }

//
//    @GetMapping("/api/v1/games")
//    public void getGameListing() {
//        gameService.getGameStateList();
//    }
//
//    @PostMapping("/api/v1/games")
//    public void createCasualGame(Principal principal) {
//        gameService.createGame(10);
//    }

    /**
     * This method getsGameInfo of a specific game and responds with the info the user needs to display it
     * @param gameID a long value representing the gameState in repository
     * @return the GameStateTransport of that game
     */
    @GetMapping("/api/v1/games/{id}")
    public GameStateTransport getGameInfo(@PathVariable("id") long gameID) {
        return gameService.getGameStateTransport(gameID);
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


}