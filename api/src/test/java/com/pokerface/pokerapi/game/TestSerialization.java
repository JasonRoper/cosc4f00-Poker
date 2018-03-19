package com.pokerface.pokerapi.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestSerialization {
    private ObjectMapper mapper;

    @Before
    public void setupObjectMapper(){
        mapper = new ObjectMapper();
    }

    @Test
    public void testGameStateTransportSerializes() throws JsonProcessingException {

        GameStateTransport testTransport = new GameStateTransport();
        testTransport.reason(GameStateTransport.Reason.HAND_STARTED, "the hand has started");
        testTransport.setPlayers(new GameStateTransport.PlayerTransport[3]);
        for (int i = 0 ; i < 3 ; i++)
            testTransport.getPlayers()[i] = new GameStateTransport.PlayerTransport(i,200,null, true, false);
        String val = mapper.writer().writeValueAsString(testTransport);

    }

    @Test
    public void testHandTransportSerilizes() throws JsonProcessingException {
        HandTransport transport = new HandTransport();
        transport.cardOne = Card.DIAMONDS_JACK;
        String val = mapper.writer().writeValueAsString(transport);

    }

    @Test
    public void testHandEndTransportSerilizes() throws JsonProcessingException {
        HandEndTransport transport = new HandEndTransport();
        String val = mapper.writer().writeValueAsString(transport);
    }

    @Test
    public void testGameInfoTransportSerilizes() throws JsonProcessingException {
        GameInfoTransport transport = new GameInfoTransport(1);
        String val = mapper.writer().writeValueAsString(transport);
    }

}
