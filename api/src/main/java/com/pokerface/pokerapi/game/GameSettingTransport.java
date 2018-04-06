package com.pokerface.pokerapi.game;

public class GameSettingTransport {
    int maxPlayer = 6; // max six
    int minimumPlayer = 4; // minimum player count, minimum is 4? prolly 3, to start the game
    int cashOnHand = 100;
    int bigBlind = 12;
    int aiPlayers = 0; //count of AI players
    GameState.GameType gameType;

    public GameSettingTransport(){

    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public int getMinimumPlayer() {
        return minimumPlayer;
    }

    public void setMinimumPlayer(int minimumPlayer) {
        this.minimumPlayer = minimumPlayer;
    }

    public int getCashOnHand() {
        return cashOnHand;
    }

    public void setCashOnHand(int cashOnHand) {
        this.cashOnHand = cashOnHand;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    public int getAiPlayers() {
        return aiPlayers;
    }

    public void setAiPlayers(int aiPlayers) {
        this.aiPlayers = aiPlayers;
    }

    public GameState.GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameState.GameType gameType) {
        this.gameType = gameType;
    }
}
