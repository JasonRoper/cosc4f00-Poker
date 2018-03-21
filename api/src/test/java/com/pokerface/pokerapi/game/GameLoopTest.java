package com.pokerface.pokerapi.game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test to see if {@link GameRepository} is functioning correctly
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class GameLoopTest {

    GameService gameService;

    @Autowired
    private GameRepository games;

    @Autowired
    private TestEntityManager testEntityManager;

    @Before
    public void setUpTest() {
        gameService = new GameService(games);
    }

    @Test
    public void newRound(){
        GameState game = games.findOne(gameService.createGame());
        long gameID=game.getId();
        game.addPlayer(10);
        game.addPlayer(11);
        game.addPlayer(12);
        game.addPlayer(13);
        game.startGame();
        gameService.handleAction(gameID,createAction(13,GameActionType.CHECK,10),gameService.getPlayerID(gameID,13));
        gameService.handleAction(gameID,createAction(10,GameActionType.CHECK,10),gameService.getPlayerID(gameID,10));
        gameService.handleAction(gameID,createAction(11,GameActionType.CHECK,10),gameService.getPlayerID(gameID,11));
        gameService.handleAction(gameID,createAction(12,GameActionType.CHECK,10),gameService.getPlayerID(gameID,12));
        gameService.handleAction(gameID,createAction(13,GameActionType.CHECK,10),gameService.getPlayerID(gameID,13));
        gameService.handleAction(gameID,createAction(10,GameActionType.CHECK,10),gameService.getPlayerID(gameID,10));
    }


    @Test
    public void testMultipleGameStart(){
        GameState game2= games.findOne(gameService.createGame());
        GameState game = games.findOne(gameService.createGame());
        long gameID=game.getId();
        game.addPlayer(1);
        game.addPlayer(2);
        game.addPlayer(3);
        game.addPlayer(4);
        game.startGame();
        gameService.handleAction(gameID,createAction(1,GameActionType.BET,10),gameService.getPlayerID(gameID,1));
        gameService.handleAction(gameID,createAction(2,GameActionType.BET,10),gameService.getPlayerID(gameID,2));
        gameService.handleAction(gameID,createAction(3,GameActionType.BET,10),gameService.getPlayerID(gameID,3));
        gameService.handleAction(gameID,createAction(4,GameActionType.BET,10),gameService.getPlayerID(gameID,4));
        game=games.findOne(gameID);
        games.save(game);
        game=games.findOne(gameID);
        gameService.handleAction(gameID,createAction(1,GameActionType.CHECK,0),gameService.getPlayerID(gameID,1));
        gameService.handleAction(gameID,createAction(2,GameActionType.BET,39230),gameService.getPlayerID(gameID,2));
        gameService.handleAction(gameID,createAction(3,GameActionType.FOLD,0),gameService.getPlayerID(gameID,3));
        System.out.println();


        gameID=game2.getId();
        game2.addPlayer(5);
        game2.addPlayer(6);
        game2.addPlayer(7);
        game2.addPlayer(8);
        game2.startGame();
        gameService.handleAction(gameID,createAction(5,GameActionType.BET,10),gameService.getPlayerID(gameID,5));
        gameService.handleAction(gameID,createAction(6,GameActionType.BET,10),gameService.getPlayerID(gameID,6));
        gameService.handleAction(gameID,createAction(7,GameActionType.BET,10),gameService.getPlayerID(gameID,7));
        gameService.handleAction(gameID,createAction(8,GameActionType.BET,10),gameService.getPlayerID(gameID,8));
        game2=games.findOne(gameID);
        games.save(game);
        game2=games.findOne(gameID);
        gameService.handleAction(gameID,createAction(5,GameActionType.CHECK,0),gameService.getPlayerID(gameID,5));
        gameService.handleAction(gameID,createAction(6,GameActionType.BET,39230),gameService.getPlayerID(gameID,6));
        gameService.handleAction(gameID,createAction(7,GameActionType.FOLD,0),gameService.getPlayerID(gameID,7));
        System.out.println();
    }

    public GameAction createAction(long userID,GameActionType actionType,int bet){
        return new GameAction(userID,actionType,bet);
    }
}