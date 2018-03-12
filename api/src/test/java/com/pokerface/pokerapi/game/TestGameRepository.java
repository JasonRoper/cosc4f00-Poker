package com.pokerface.pokerapi.game;

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

    @Autowired
    private GameRepository games;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testQuery(){
        GameService gameService = new GameService(games);
        testEntityManager.persist(new GameState());
        testEntityManager.persist(new GameState());
        testEntityManager.persist(new GameState());
        testEntityManager.persist(new GameState());
        testEntityManager.persist(new GameState());
        assertNotEquals(gameService.matchmake(1),0);
    }
}
