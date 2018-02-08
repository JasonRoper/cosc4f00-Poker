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
    private Long id; // id to the database
    private int gameID; // id to the game
    private GameState gameState;

    public Player(){}

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

    public int getGameID() { return gameID; }

    public void setgameID(int gameID) { this.gameID=gameID; }

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




}



