package com.pokerface.pokerapi.game;

import com.pokerface.pokerapi.users.UserInfoTransport;
import com.pokerface.pokerapi.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;
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
@Controller
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
     *
     */
    @MessageMapping("/game/{game_id}/control")
    public void receiveManagementAction() {

    }


    @MessageMapping("/game/{game_id}")
    public void receiveAction(GameAction action, @DestinationVariable("game_id") long gameId, Principal principal) {

        UserInfoTransport user = userService.getUser(principal.getName());
        int playerId = gameService.getPlayerID(gameId, user.getId());

        GameStateTransport nextGameState = handleAction(gameId, action, playerId);

        while (aiService.isAIPlayer(gameId, nextGameState.nextPlayer())) {
            GameAction aiAction = aiService.playAction(gameId);
            nextGameState = handleAction(gameId, aiAction, nextGameState.nextPlayer());
        }
    }

    private GameStateTransport handleAction(long gameId, GameAction action, int playerId) {
        GameStateTransport nextGameState = gameService.handleAction(gameId, action, playerId);
        messenger.convertAndSend("/messages/game/" + gameId, nextGameState);

        if (gameService.isHandEnd(gameId)) {
            HandEndTransport winners = gameService.determineWinnings(gameId);
            nextGameState = gameService.getGameState(gameId);
            messenger.convertAndSend("/messages/game/" + gameId,
                    nextGameState.reason(GameStateTransport.Reason.HAND_FINISHED,""));
        } else if (gameService.isRoundEnd(gameId)){
            nextGameState = gameService.handleRound(gameId);
            messenger.convertAndSend("/messages/game/" + gameId,
                    nextGameState.reason(GameStateTransport.Reason.ROUND_FINSHED,""));
        }

        return nextGameState;
    }

//    @PostMapping("/api/v1/matchmaking/basicGame")
//    public void createGame(Principal principal) {
//        UserInfoTransport user = userService.getUser(principal.getName());
//        gameService.matchmake(user.getId());
//
//    }
//
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