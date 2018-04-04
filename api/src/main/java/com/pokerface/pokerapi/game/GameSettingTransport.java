package com.pokerface.pokerapi.game;

public class GameSettingTransport {
    int maxPlayer; // max six
    int minimumPlayer; // minimum player count, minimum is 4? prolly 3, to start the game
    int cashOnHand;
    int bigBlind;
    int aiPlayers; //count of AI players

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
}
