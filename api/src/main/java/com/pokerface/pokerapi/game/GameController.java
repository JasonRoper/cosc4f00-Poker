package com.pokerface.pokerapi.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Stack;


@Controller
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    GameRepository games;
    GameService gameService = new GameService(games);

    @Autowired
    public void setGameRepository(GameRepository games){
        this.games = games;
    }


    /**
     * This function is the baseline for when a player asks to be placed into a match. If there is a suitable game for them, it finds it and adds them, if there isn't it makes the game and will add other players to it.
     * @param id
     * @return
     */
    public long matchmake(long id){
        long gameID= - 0;
        gameID=firstAvailableGame();
        if (gameID==0){
            gameID=createGame();
        }
        return gameID;
    }

    /**
     * This function adds players, and checks that the starting requirements have been met, thus starting the game.
     * @param playerID
     * @param gameID
     */
    private void addPlayer(long playerID, long gameID){
        GameState game=games.findOne(gameID);
        if (game.addPlayer(playerID)>=game.minPlayerCount){
            startGame(game.getId());
        }
        games.save(game);
    }

    /**
     * This function starts games once the amount of users reached is enough. This is done once in the lifetime of a game
     * @param ID This parameter is the games id being started
     */
    private void startGame(long ID){

    }

    private long firstAvailableGame(){
       // return games.getNextGame();
return 0;
    }


        private long createGame() {
        GameState state = new GameState();
        return games.save(state).getId();
    }

//    @MessageMapping("/game/{id}/play")
//    @SendTo("/messages/game/{id}")
//    public GameUpdateTransport play(GameAction action, @DestinationVariable("id") Long id) {
//        GameState state = games.findOne(id);
//        logger.info("recieved play action for " + id);
//        if (state == null) {
//            logger.info("invalid path request: " + id);
//            throw new IllegalArgumentException("invalid id");
//        }
//
//return null;
//    }
//
//    @MessageExceptionHandler
//    @SendToUser("/messages/errors")
//    public String handleException(IllegalArgumentException e){
//        return e.toString();
//    }
//
//    @RequestMapping(value = "/game/new/{id}",method = )
//    @ResponseBody
//    public String createGame(@PathVariable("id") long id) {
//        logger.info("new game created at " + id);
//        GameState state = new GameState(id);
//        games.save(state);
//        return "success! game created at " + Long.toString(state.getId());
//    }
}
