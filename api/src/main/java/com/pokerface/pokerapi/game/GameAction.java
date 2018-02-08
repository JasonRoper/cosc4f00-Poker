package com.pokerface.pokerapi.game;

public class GameAction {
    private GameActionType type;
    private int bet;

    public GameActionType getType (){
        return type;
    }

    public int getBet () {
        return bet;
    }
}
