package com.pokerface.pokerapi.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private GameRepository games;

    @Autowired
    public GameService(GameRepository games) {
        this.games = games;
    }
}