package com.pokerface.pokerapi.game;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * A Player representation, containing an id, their seat, the money they have, the action they last performed, if they are a player or if they are a dealer.
 */
public class PlayerTransport {
    private int playerID;
    private int money;
    private String name;
    private GameAction action;
    private boolean isPlayer;
    private boolean isDealer;
    private boolean isFold;
    private Card cardOne;
    private Card cardTwo;
    private Integer winnings;
    private int currentBet;

    public PlayerTransport() {

    }

    /**
     * Construct a {@link PlayerTransport} with the given data
     * @param id the player id of this player
     * @param money the amount of money this player has
     * @param action the last action that this player took
     * @param isPlayer whether or not the player is a player or an AI
     * @param isDealer whether or not this player is the dealer
     */
    public PlayerTransport(int id, int money, GameAction action, boolean isPlayer, boolean isDealer,boolean isFold,int amountBet,String name) {
        this.playerID = id;
        this.money = money;
        this.action = action;
        this.isPlayer = isPlayer;
        this.isDealer = isDealer;
        this.isFold=isFold;
        cardOne=null;
        cardTwo=null;
        this.currentBet=amountBet;
        this.name=name;
    }

    public PlayerTransport(int id, int money, GameAction action, boolean isPlayer, boolean isDealer,boolean isFold, int winnings, Card cardOne, Card cardTwo,int amountBet, String name){
        this(id,money,action,isPlayer,isDealer,isFold,amountBet,name);
        this.winnings=winnings;
        this.cardOne=cardOne;
        this.cardTwo=cardTwo;
    }

    public int getId() {
        return playerID;
    }

    public void setId(int id) {
        this.playerID = id;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public GameAction getAction() {
        return action;
    }

    public void setAction(GameAction action) {
        this.action = action;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public void setPlayer(boolean player) {
        isPlayer = player;
    }

    public boolean isDealer() {
        return isDealer;
    }

    public void setDealer(boolean dealer) {
        isDealer = dealer;
    }

    public boolean isFold() {
        return isFold;
    }

    public void setFold(boolean fold) {
        isFold = fold;
    }

    public Card getCardOne() {
        return cardOne;
    }

    public void setCardOne(Card cardOne) {
        this.cardOne = cardOne;
    }

    public Card getCardTwo() {
        return cardTwo;
    }

    public void setCardTwo(Card cardTwo) {
        this.cardTwo = cardTwo;
    }

    public Integer getWinnings() {
        return winnings;
    }

    public void setWinnings(Integer winnings) {
        this.winnings = winnings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerTransport that = (PlayerTransport) o;
        return playerID == that.playerID &&
                getMoney() == that.getMoney() &&
                isPlayer() == that.isPlayer() &&
                isDealer() == that.isDealer() &&
                isFold() == that.isFold() &&
                currentBet == that.currentBet &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getAction(), that.getAction()) &&
                getCardOne() == that.getCardOne() &&
                getCardTwo() == that.getCardTwo() &&
                Objects.equals(getWinnings(), that.getWinnings());
    }

    @Override
    public int hashCode() {

        return Objects.hash(playerID, getMoney(), getName(), getAction(), isPlayer(), isDealer(), isFold(), getCardOne(), getCardTwo(), getWinnings(), currentBet);
    }
}