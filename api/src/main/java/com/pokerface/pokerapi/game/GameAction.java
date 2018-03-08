package com.pokerface.pokerapi.game;

public class GameAction {
    private GameActionType type;
    private int bet;

    public GameAction(){

    }

    public GameAction(GameActionType type, int bet){
        this.type=type;
        this.bet=bet;
    }

    public GameActionType getType (){
        return type;
    }

    public int getBet () {
        return bet;
    }
}
