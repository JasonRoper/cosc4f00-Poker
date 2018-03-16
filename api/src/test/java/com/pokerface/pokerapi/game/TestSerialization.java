package com.pokerface.pokerapi.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class TestSerialization {
    @Test
    public void testGameStateTransportSerializes() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        GameStateTransport testTransport = new GameStateTransport();
        testTransport.setNextPlayer(1);
        testTransport.reason(GameStateTransport.Reason.HAND_STARTED, "the hand has started");
        testTransport.setBigBlind(100);
        testTransport.setPlayers(new GameStateTransport.PlayerTransport[3]);
        for (int i = 0 ; i < 3 ; i++)
            testTransport.getPlayers()[i] = new GameStateTransport.PlayerTransport(i,200,null, true, false);
        mapper.writer().writeValueAsString(testTransport);
    }
}
