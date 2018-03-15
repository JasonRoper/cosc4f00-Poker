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
public class TestGameRepository {

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
        GameStateTransport transport2=gameService.getGameStateTransport(game.getId());
        System.out.println();
    }

}
