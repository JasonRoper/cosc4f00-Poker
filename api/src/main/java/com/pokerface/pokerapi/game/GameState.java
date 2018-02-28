package com.pokerface.pokerapi.game;

import javax.persistence.*;
import java.util.List;

@Entity
/**
 * GameState stores everything necessary to know about the state of a game. It should do little, but represent the database object
 */
public class GameState {
    private long id;
    private Deck deck; //A 1-1 storage of a stack full of enum cards.
    private Pot pot; // Each represents how much each player has put in.
    //First round, the flop/ the turn/the river
    private int lastBet; //PlayerTableID
    private int dealer; //PlayerTableID
    private int minimumBet; // the minimum bet required to stay in the round
    private int bigBlind;
    private int presentTurn;//Whose action is it?
    private int round;//What round are we on, enum? from 0-4, 0 transition value? 1 pre-bet, 2/3/4 is flop turn river respectively.
    private List<Player> players;
    private boolean showdown=false; // Are we in the final showdown

    public GameState(){
    }

    public GameState(long id) {
        setId(id);
    }

    public GameState(Deck deck) {
        this.deck = deck;
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
    public Pot getPot() {
        return pot;
    }

    public void setPot(Pot pot) {
        this.pot = pot;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setLastBet(int lastBet) {
        this.lastBet = lastBet;
    }

    public void setDealer(int dealer) {
        this.dealer = dealer;
    }

    public void setMinimumBet(int minimumBet) {
        this.minimumBet = minimumBet;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    public void setPresentTurn(int presentTurn) {
        this.presentTurn = presentTurn;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getLastBet() {
        return lastBet;
    }

    public int getDealer() {
        return dealer;
    }

    public int getMinimumBet() {
        return minimumBet;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public int getPresentTurn() {
        return presentTurn;
    }

    public int getRound() {
        return round;
    }

    @OneToMany(mappedBy = "gameState", cascade = CascadeType.ALL)
    @OrderColumn
    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players= players;
    }

    public boolean matchBet (int playerSeatID){
        Player player = players.get(playerSeatID);
        double difference=minimumBet-pot.getBet(playerSeatID);
        if (player.getCashOnHand()>=difference){
            pot.add(difference,playerSeatID);
            player.setCashOnHand(player.getCashOnHand()-difference);
        } else {
            return false;
        }

        return true;
    }

    public boolean placeBet(int playerSeatID, double betAmount){
        Player player=players.get(playerSeatID);
        if (player.getCashOnHand()>=betAmount){
            pot.add(betAmount,playerSeatID);
            player.setCashOnHand(player.getCashOnHand()-betAmount);
            lastBet=playerSeatID;
            minimumBet+=betAmount;
        } else {
            return false;
        }
       return true;
    }
}
