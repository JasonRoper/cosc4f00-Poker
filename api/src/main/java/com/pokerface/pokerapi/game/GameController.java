package com.pokerface.pokerapi.game;

import com.pokerface.pokerapi.users.UserInfoTransport;
import com.pokerface.pokerapi.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Stack;


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

    @MessageMapping("/game/{game_id}/control")
    public void receiveManagementAction() {

    }



    @MessageMapping("/game/{game_id}")
    public void receiveAction(GameAction action, @DestinationVariable("game_id") long id, Principal principal) {
        UserInfoTransport user = userService.getUser(principal.getName());
        int playerId = gameService.getPlayerID(user.getId());
        GameStateTransport nextGameState = gameService.handleAction(id, action, user.getId());
        messenger.convertAndSend("/messages/game/" + id, nextGameState);

        while (aiService.isAIPlayer(nextGameState).getNextId()) {
            GameAction aiAction = aiService.playAction(nextGameState);
            nextGameState = gameService.handleAction(id, aiAction, nextGameState.getNextId());
            messenger.convertAndSend("/messages/game/" + id, nextGameState);
        }
    }

    @PostMapping("/api/v1/matchmaking/basicGame")
    public void createGame(Principal principal) {
        UserInfoTransport user = userService.getUser(principal.getName());
        gameService.matchmake(user.getId());

    }

    @GetMapping("/api/v1/games")
    public void getGameListing() {

    }

    @PostMapping("/api/v1/games")
    public void createCasualGame(Principal principal) {

    }

    @GetMapping("/api/v1/games/{id}")
    public void getGameInfo(@PathVariable("id") long gameId) {

    }

    @PutMapping("/api/v1/games/{id}")
    public void updateGameRules(@PathVariable("id") long gameId) {

    }

    @DeleteMapping("/api/v1/games/{id}")
    public void deleteOwnedGame(@PathVariable("id") long gameId) {

    }
}