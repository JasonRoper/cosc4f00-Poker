package com.pokerface.pokerapi.game;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GameDoesNotExistException extends RuntimeException {
    private final long gameID;

    public GameDoesNotExistException(long gameID) {
        super("the game at gameID " + gameID + " does not exist");
        this.gameID = gameID;
    }

    public long getGameID() {
        return gameID;
    }
}
