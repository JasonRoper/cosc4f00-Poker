package com.pokerface.pokerapi.game;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A player representation that lives in the GameState
 * They have two card objects, an ID tied to the database of the user who is playing them, the seatID of where they are
 * in the game, the GameState they belong to, if they have folded and if they are AI
 */
@Entity
@Table(name = "player")
public class Player {
    @Enumerated
    private Card cardOne;
    @Enumerated
    private Card cardTwo;
    private int cashOnHand;//User available money in game
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_ID")
    private long ID;
    private long userID; // id to the database
    private int playerID; // id to the game
    private boolean hasFolded;
    private boolean isAI;
    private boolean isDealer;
    private boolean isAllIn;
    private int bet;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private GameAction lastGameAction=null;
    private String name="Default";



    public Player(){
    }

    public Player (Long userID, GameState gameState,String name){
        this(userID,gameState);
        this.name=name;
    }

    /**
     * Creates a player with a userID
     * @param userID is the long UserID of the player
     */
    public Player(Long userID, GameState gameState){
        lastGameAction=new GameAction(null,0,this);
        this.userID=userID;
        cashOnHand=gameState.getDefaultCashOnHand();
        hasFolded=false;
        isDealer=false;
        isAI=false;
        isAllIn=false;
        this.name="Default";
    }

    public List<Card> receiveCards(){
        List<Card> cards =new ArrayList<>();
        cards.add(cardOne);
        cards.add(cardTwo);
        return cards;
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
    public int getPlayerID() { return playerID; }

    /**
     * sets the TabelSeatID to the value
     * @param playerID int representing the PlayerID
     */
    public void setPlayerID(int playerID) { this.playerID =playerID; }


    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    /**
     * The ID of where this object exists in the database
     * @return the long userID of the Player
     */



    public long getUserID() {
        return userID;
    }

    /**
     * sets the playerID
     * @param userID long value to set
     */
    public void setUserID(long userID) {
        this.userID = userID;
    }

    /**
     * sets that they have folded
     * @param state boolean of if they have folded
     */
    public void setHasFolded(boolean state){hasFolded=state;}

    /**
     * Gets if the user has folded during this hand or not
     * @return boolean value representing if they have folded
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

    public boolean isAllIn() {
        return isAllIn;
    }

    public void setAllIn(boolean allIn) {
        isAllIn = allIn;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public void addBet(int amount){
        bet+=amount;
    }

    public boolean isDealer() {
        return isDealer;
    }

    public void setDealer(boolean dealer) {
        isDealer = dealer;
    }

    public GameAction getLastGameAction() {
        return lastGameAction;
    }

    public void setLastGameAction(GameAction lastGameAction) {
this.lastGameAction=lastGameAction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateLastGameAction(GameAction lastGameAction){
        this.lastGameAction.setType(lastGameAction.getType());
        this.lastGameAction.setBet(lastGameAction.getBet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return cashOnHand == player.cashOnHand &&
                ID == player.ID &&
                userID == player.userID &&
                playerID == player.playerID &&
                hasFolded == player.hasFolded &&
                isAI == player.isAI &&
                isDealer == player.isDealer &&
                isAllIn == player.isAllIn &&
                bet == player.bet &&
                cardOne == player.cardOne &&
                cardTwo == player.cardTwo &&
                Objects.equals(lastGameAction, player.lastGameAction) &&
                Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(cardOne, cardTwo, cashOnHand, ID, userID, playerID, hasFolded, isAI, isDealer, isAllIn, bet, lastGameAction, name);
    }
}



