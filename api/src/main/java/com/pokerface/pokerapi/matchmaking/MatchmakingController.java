package com.pokerface.pokerapi.matchmaking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.pokerface.pokerapi.game.GameRepository;

@Controller
public class MatchmakingController {


    private GameRepository gameRepository;

    public MatchmakingController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @MessageMapping("/matchmaking/start")
    public void startMatchmaking() {
        
    }

    @MessageMapping("/matchmaking/stop")
    public void stopMatchmaking() {
        
    }
}