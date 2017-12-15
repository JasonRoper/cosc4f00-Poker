package com.pokerface.pokerapi.game;

import javax.persistence.*;
import java.util.List;
import java.util.Stack;

@Entity
/**
 * GameState stores everything necessary to know about the state of a game. It should do little, but represent the database object
 */
public class GameState {
    private long id;
    private Deck deck; //A 1-1 storage of a stack full of enum cards.
    int[] pot; // Each represents how much each player has put in.
    //First round, the flop/ the turn/the river
    boolean hasBet; // Unecessary? Could be mapped to lastBet
    int lastBet; //PlayerTableID
    int dealer; //PlayerTableID
    int nextTurnID;
    int minimumBet;
    int bigBlind;
    int[] cashOnHand;//User available money in game
    int presentTurn;//Whose action is it?
    int round;//What round are we on, enum? from 0-4, 0 transition value? 1 pre-bet, 2/3/4 is flop turn river respectively.


    public GameState(){

    }

    public GameState(long id) {
        setId(id);
    }

    public GameState(Deck deck) {
        this.deck = deck;
    }

    public void play(GameAction action) {
        nextTurnID++;
    }

    public GameTransport toTransport() {
        GameTransport transport = new GameTransport();
        transport.setNextId(nextTurnID);
        //transport.setPot(potSum);
        return transport;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setPot(int[] pot) {
        this.pot = pot;
    }

    public void setHasBet(boolean hasBet) {
        this.hasBet = hasBet;
    }

    public void setLastBet(int lastBet) {
        this.lastBet = lastBet;
    }

    public void setDealer(int dealer) {
        this.dealer = dealer;
    }

    public void setNextTurnID(int nextTurnID) {
        this.nextTurnID = nextTurnID;
    }

    public void setMinimumBet(int minimumBet) {
        this.minimumBet = minimumBet;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    public void setCashOnHand(int[] cashOnHand) {
        this.cashOnHand = cashOnHand;
    }

    public void setPresentTurn(int presentTurn) {
        this.presentTurn = presentTurn;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int[] getPot() {
        return pot;
    }

    public boolean isHasBet() {
        return hasBet;
    }

    public int getLastBet() {
        return lastBet;
    }

    public int getDealer() {
        return dealer;
    }

    public long getNextTurnID() {
        return nextTurnID;
    }

    public int getMinimumBet() {
        return minimumBet;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public int[] getCashOnHand() {
        return cashOnHand;
    }

    public int getPresentTurn() {
        return presentTurn;
    }

    public int getRound() {
        return round;
    }
}
