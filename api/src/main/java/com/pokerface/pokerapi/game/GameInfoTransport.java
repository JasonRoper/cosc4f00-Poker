package com.pokerface.pokerapi.game;

public class GameInfoTransport {
    private long gameId;

    public GameInfoTransport() {

    }

    public GameInfoTransport(long gameId) {
        this.gameId = gameId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }
}