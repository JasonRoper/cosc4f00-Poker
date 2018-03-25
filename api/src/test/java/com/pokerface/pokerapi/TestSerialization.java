package com.pokerface.pokerapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokerface.pokerapi.game.*;
import com.pokerface.pokerapi.users.UserInfoTransport;
import com.pokerface.pokerapi.users.UserTransport;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestSerialization {
    private ObjectMapper mapper;

    @Before
    public void setupObjectMapper(){
        mapper = new ObjectMapper();
    }

    @Test
    public void testGameStateTransportSerialization() throws IOException {
        assertTrue("can not serialize", mapper.canSerialize(GameStateTransport.class));
        assertTrue("can not deserialize", mapper.canDeserialize(mapper.constructType(GameStateTransport.class)));
        GameStateTransport testGameState =  new GameStateTransport();

        GameStateTransport gameState = new GameStateTransport();


//int id, int money, GameAction action, boolean isPlayer, boolean isDealer,boolean isFold,int amountBet
        gameState.setPlayers(new PlayerTransport[]{
                new PlayerTransport(
                        1,
                        200,
                        new GameAction(GameActionType.BET, 1),
                        true,
                        true,
                        true,
                        0,
                        "Sal"
                ), // admin
                new PlayerTransport(
                        2,
                        10000,
                        new GameAction(GameActionType.BET, 1),
                        true,
                        false,
                        true,
                        0,
                        "Fred"
                )}); // jason
        gameState.setNextPlayer(1);
        gameState.setBigBlind(10);
        gameState.setCommunityCards(Arrays.asList(Card.SPADES_QUEEN, Card.SPADES_SEVEN, Card.SPADES_KING));
        gameState.setPotSum(30);
        gameState.reason(GameStateTransport.Reason.PLAYER_ACTION, "");

        String serialized = mapper.writer().writeValueAsString(gameState);
        GameStateTransport res = mapper.readValue(serialized, mapper.constructType(GameStateTransport.class));
    }

    @Test
    public void testHandTransportSerialization() throws IOException {
        assertTrue("can not serialize", mapper.canSerialize(HandTransport.class));
        assertTrue("can not deserialize", mapper.canDeserialize(mapper.constructType(HandTransport.class)));

        HandTransport transport = new HandTransport();
        transport.setCardOne(Card.CLUBS_EIGHT);
        String serialized = mapper.writeValueAsString(transport);
        HandTransport res = mapper.readValue(serialized, mapper.constructType(HandTransport.class));
    }

    @Test
    public void testHandEndTransportSerialization() throws IOException {
        assertTrue("can not serialize", mapper.canSerialize(HandEndTransport.class));
        assertTrue("can not deserialize", mapper.canDeserialize(mapper.constructType(HandEndTransport.class)));
        HandEndTransport transport = new HandEndTransport();
        transport.setPlayers(new PlayerTransport[] {
                new PlayerTransport(1, 100, new GameAction(GameActionType.CALL,1), true, false, false, 19, "John"),
                new PlayerTransport(2, 100, new GameAction(GameActionType.CALL,1), true, false, false, 19, "Jake"),
        });
        String serialized = mapper.writeValueAsString(transport);
        HandEndTransport res = mapper.readValue(serialized, mapper.constructType(HandEndTransport.class));
    }

    @Test
    public void testGameInfoTransportSerialization() throws IOException {
        assertTrue("can not serialize", mapper.canSerialize(GameInfoTransport.class));
        assertTrue("can not deserialize", mapper.canDeserialize(mapper.constructType(GameInfoTransport.class)));
        String serialized = mapper.writer().writeValueAsString(new GameInfoTransport(1));
        GameInfoTransport res = mapper.readValue(serialized, mapper.constructType(GameInfoTransport.class));
    }

    @Test
    public void testUserTransportSerialization() throws IOException {
        assertTrue("can not serialize", mapper.canSerialize(UserTransport.class));
        assertTrue("can not deserialize", mapper.canDeserialize(mapper.constructType(UserTransport.class)));
        String serialized = mapper.writer().writeValueAsString(new UserTransport(1, "Jason", "testing@test.ca"));
        UserTransport res = mapper.readValue(serialized, mapper.constructType(UserTransport.class));
    }

    @Test
    public void testUserInfoTransportSerialization() throws IOException {
        assertTrue("can not serialize", mapper.canSerialize(UserInfoTransport.class));
        assertTrue("can not deserialize", mapper.canDeserialize(mapper.constructType(UserInfoTransport.class)));
        String serialized = mapper.writer().writeValueAsString(new UserInfoTransport(1, "Jason"));
        UserInfoTransport res = mapper.readValue(serialized, mapper.constructType(UserInfoTransport.class));
    }
}
