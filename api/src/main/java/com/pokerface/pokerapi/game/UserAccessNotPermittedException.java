package com.pokerface.pokerapi.game;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserAccessNotPermittedException extends RuntimeException {
    private final long gameID;

    public UserAccessNotPermittedException(String username, long gameID, String resource) {
        super("user " + username + " is not permitted to access to " + resource + " for game " + gameID);
        this.gameID = gameID;
    }

    public long getGameID() {
        return gameID;
    }
}
