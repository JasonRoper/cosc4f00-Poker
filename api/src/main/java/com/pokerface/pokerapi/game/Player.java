package com.pokerface.pokerapi.game;

import javax.persistence.*;

@Entity
@Table(name = "player")
public class Player {
    @Enumerated
    private Card cardOne;
    @Enumerated
    private Card cardTwo;
    private int cashOnHand;//User available money in game
    private long id; // id to the database
    private int tableSeatID; // id to the game
    private GameState gameState;
    private boolean hasFolded;
    //private GameAction lastAction;



    private boolean isDealer;


    public Player(Long id){
        this.id=id;
        cashOnHand=getGameState().defaultCashOnHand;
        hasFolded=false;
        isDealer=false;
    }

    public Card getCardOne(){return cardOne;}

    public void setCardOne(Card card){
        cardOne=card;
    }

    public Card getCardTwo(){return cardTwo;}

    public void setCardTwo(Card card){
        cardTwo=card;
    }

    public int getCashOnHand() {
        return cashOnHand;
    }

    public void setCashOnHand(int cashOnHand) {
        this.cashOnHand = cashOnHand;
    }

    public int getTableSeatID() { return tableSeatID; }

    public void setTableSeatID(int tableSeatID) { this.tableSeatID =tableSeatID; }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="gameState")
    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setHasFolded(boolean state){hasFolded=state;}

    public boolean getHasFolded() {return hasFolded;}

    public void addPlayer(){

    }

//    public GameAction getLastAction() {
//        return lastAction;
//    }
//
//    public void setLastAction(GameAction lastAction) {
//        this.lastAction = lastAction;
//    }

    public boolean getIsDealer() {
        return isDealer;
    }

    public void setIsDealer(boolean dealer) {
        isDealer = dealer;
    }

}



