package com.pokerface.pokerapi.game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Test to see if {@link GameRepository} is functioning correctly
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class GameServiceRepositoryTest {

    GameService gameService;

    @Autowired
    private GameRepository games;

    @Autowired
    private TestEntityManager testEntityManager;

    @Before
    public void setUpTest(){
        gameService = new GameService(games);
    }

    @Test
    public void testQuery(){
        testEntityManager.persist(new GameState());
        testEntityManager.persist(new GameState());
        testEntityManager.persist(new GameState());
        testEntityManager.persist(new GameState());
        testEntityManager.persist(new GameState());
        assertNotEquals(gameService.matchmake(1),0);
    }

    /**
     * This test not only tests matchmake but also addPlayer, createGame and the repositories custom query as well
     * as its ability to save new game updates.
     */
    @Test
    public void testMatchmake(){
        long id = gameService.matchmake(1);
        assertNotEquals(id,0);
    }

    @Test
    public void testNullGameStateTransport(){
        GameState game = games.findOne(gameService.createGame());
        GameStateTransport transport=gameService.getGameStateTransport(game.getId());
        System.out.println();
    }

    @Test
    public void testWaitingGameStateTransport(){
        GameState game = games.findOne(gameService.createGame());
        game.addPlayer(1);
        game.addPlayer(2);
        game.addPlayer(3);
        GameStateTransport transport=gameService.getGameStateTransport(game.getId());
        System.out.println();
    }

    /**
     * Tests the game starting state and the resulting transports
     */
    @Test
    public void testStartingGameStateTransport(){
        GameState game = games.findOne(gameService.createGame());
        game.addPlayer(1);
        game.addPlayer(2);
        game.addPlayer(3);
        game.addPlayer(4);
        GameStateTransport transport=gameService.gameStart(game.getId());
        HandTransport hand;
        for (Player p:game.getPlayers()){
            hand=new HandTransport(p.getCardOne(),p.getCardTwo());
        }

        System.out.println();
    }

    @Test
    public void testGamePlay(){
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
        System.out.println();
    }

    public GameAction createAction(long userID,GameActionType actionType,int bet){
        return new GameAction(userID,actionType,bet);
    }

}
