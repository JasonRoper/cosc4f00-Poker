package com.pokerface.pokerapi.matchmaking;

import com.pokerface.pokerapi.game.GameService;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MatchmakingService {
    private SimpMessagingTemplate messagingClient;
    private GameService games;

    public MatchmakingService(SimpMessagingTemplate messaging, GameService gameService) {
        messagingClient = messaging;
        this.games = gameService;
    }
}
