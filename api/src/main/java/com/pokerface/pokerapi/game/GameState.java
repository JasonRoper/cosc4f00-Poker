package com.pokerface.pokerapi.game;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.Stack;

@Entity
public class GameState {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    long id; // The GameID
    //Deck deck; //
    int[] pot; // Each represents how much each player has put in.
    //First round, the flop/ the turn/the river
    boolean hasBet; // Unecessary?
    int lastBest; //PlayerTableID
    int dealer; //PlayerTableID
    long nextTurnId;
    int minimumBet;
    int bigBlind;
    int[] cashOnHand;//User available money in game
    int presentTurn;//Whose action is it?
    int round;//What round are we on, enum? from 0-4, 0 transition value? 1 pre-bet, 2/3/4 is flop turn river respectively.

    public GameState(){

    }

    public GameState(long id) {
        this.id = id;
    }

    public void play(GameAction action) {
        nextTurnId++;
    }

    public GameTransport toTransport() {
        GameTransport transport = new GameTransport();
        transport.setNextId(nextTurnId);
        //transport.setPot(potSum);
        return transport;
    }

}
