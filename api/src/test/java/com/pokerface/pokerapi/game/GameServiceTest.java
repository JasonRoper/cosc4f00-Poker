package com.pokerface.pokerapi.game;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;

public class GameServiceTest {
    GameService gameService;
    GameRepository gameRepository;

    @Before
    public void setUp(){
        gameRepository=Mockito.mock(GameRepository.class);
        Mockito.doAnswer(returnsFirstArg()).when(gameRepository).save(any(GameState.class));
        gameService=new GameService(gameRepository);
    }

    @Test
    public void testMatchMake(){
        assertEquals(gameService.matchmake(1),0);

    }



}
