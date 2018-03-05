package com.pokerface.pokerapi.game;

import javax.persistence.*;

@Entity
@Table(name = "player")
public class Player {
    @Enumerated
    private Card cardOne;
    @Enumerated
    private Card cardTwo;
    private double cashOnHand;//User available money in game
    private Long id; // id to the database
    private int tableSeatID; // id to the game
    private GameState gameState;
    private boolean hasFolded;

    public Player(Long id){
        this.id=id;
        cashOnHand=getGameState().defaultCashOnHand;
        hasFolded=false;
    }

    public Card getCardOne(){return cardOne;}

    public void setCardOne(Card card){
        cardOne=card;
    }

    public Card getCardTwo(){return cardTwo;}

    public void setCardTwo(Card card){
        cardTwo=card;
    }

    public double getCashOnHand() {
        return cashOnHand;
    }

    public void setCashOnHand(double cashOnHand) {
        this.cashOnHand = cashOnHand;
    }

    public int getTableSeatID() { return tableSeatID; }

    public void setTableSeatID(int tableSeatID) { this.tableSeatID =tableSeatID; }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public void setFolded(boolean state){hasFolded=state;}

    public boolean hasFolded() {return hasFolded;}

    public void addPlayer(){

    }




}



