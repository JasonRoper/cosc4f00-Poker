package com.pokerface.pokerapi.game;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CardAccessNotPermittedException extends RuntimeException {
    private final long gameID;

    public CardAccessNotPermittedException(String username, long gameID) {
        super("user " + username + " is not permitted to access cards for game " + gameID);
        this.gameID = gameID;
    }

    public long getGameID() {
        return gameID;
    }
}
