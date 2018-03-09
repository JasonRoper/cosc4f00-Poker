package com.pokerface.pokerapi.game;

import javax.persistence.*;

/**
 * A player representation that lives in the GameState
 * They have two card objects, an ID tied to the database of the user who is playing them, the seatID of where they are in the game, the GameState they belong to, if they have folded and if they are AI
 */
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
    private boolean isAI;
    private boolean isDealer;

    /**
     * Creates a player with a userID
     * @param id
     */
    public Player(Long id){
        this.id=id;
        cashOnHand=getGameState().defaultCashOnHand;
        hasFolded=false;
        isDealer=false;
        isAI=false;
    }

    /**
     * returns their first card
     * @return their first card
     */
    public Card getCardOne(){return cardOne;}

    /**
     * Sets the players first card
     * @param card the card to be set
     */
    public void setCardOne(Card card){
        cardOne=card;
    }

    /**
     * Returns their second card
     * @return the player's second card
     */
    public Card getCardTwo(){return cardTwo;}

    /**
     * sets the second card
     * @param card the card being set
     */
    public void setCardTwo(Card card){
        cardTwo=card;
    }

    /**
     * returns the amount of cash the user
     * @return integer value of their cash
     */
    public int getCashOnHand() {
        return cashOnHand;
    }

    /**
     * set their cash on hand
     * @param cashOnHand the integer of how much cash they have
     */
    public void setCashOnHand(int cashOnHand) {
        this.cashOnHand = cashOnHand;
    }

    /**
     * returns their table seat ID
     * @return the int representing the id of their seat their PlayerID
     */
    public int getTableSeatID() { return tableSeatID; }

    /**
     * sets the TabelSeatID to the value
     * @param tableSeatID int representing the PlayerID
     */
    public void setTableSeatID(int tableSeatID) { this.tableSeatID =tableSeatID; }

    /**
     * The ID of where this object exists in the database
     * @return
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    /**
     * sets the playerID
     * @param id long value to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * returns the gameState where they exist
     * @return
     */
    @ManyToOne
    @JoinColumn(name="gameState")
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Sets the GameState where they exist
     * @param gameState the GameState to set
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * sets that they have folded
     * @param state boolean of if they have folded
     */
    public void setHasFolded(boolean state){hasFolded=state;}

    /**
     * Checks if they have folde
     * @returnd boolean of if they have folded
     */
    public boolean getHasFolded() {return hasFolded;}

    /**
     * returns if they are a dealer
     * @return boolean of if they are a ealer
     */
    public boolean getIsDealer() {
        return isDealer;
    }

    /**
     * setIsDealer sets if they are a dealer
     * @param dealer boolean of if they are a dealer
     */
    public void setIsDealer(boolean dealer) {
        isDealer = dealer;
    }

    /**
     * Checks if the player is an AI
     * @return boolean of if they are an AI
     */
    public boolean isAI() {
        return isAI;
    }

    /**
     * Sets if they are an AI
     * @param AI boolean of if they are an AI
     */
    public void setAI(boolean AI) {
        isAI = AI;
    }

}



