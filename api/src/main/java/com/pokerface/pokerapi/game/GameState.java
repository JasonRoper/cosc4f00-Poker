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

    private int presentTurn;//Whose action is it?
    private int round;//What round are we on, enum? from 0-4, 0 transition value? 1 pre-bet, 2/3/4 is flop turn river respectively.
    private List<Player> players;
    private int playerCount=0;
    private List<GameAction> lastGameActions;



    /**
     * These are game settings
     *
     */
    private int bigBlind;
    int minPlayerCount;
    int defaultCashOnHand;


    public GameState(){

    }

    public GameState(long id) {
        this(id,12,4,200);
        setId(id);
    }

    public GameState(long id,int bigBlind,int minPlayerCount, int defaultCashOnHand){
        setId(id);
        this.lastBet=2;
        this.bigBlind=bigBlind;
        this.dealer=0;
        this.round=0;
        this.presentTurn=3;
        this.minPlayerCount=minPlayerCount;
        this.defaultCashOnHand=defaultCashOnHand;
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

    public void setMinPlayerCount(int minPlayerCount) {
        this.minPlayerCount = minPlayerCount;
    }

    public void setDefaultCashOnHand(int defaultCashOnHand) {
        this.defaultCashOnHand = defaultCashOnHand;
    }

    public int getMinPlayerCount() {
        return minPlayerCount;
    }

    public int getDefaultCashOnHand() {
        return defaultCashOnHand;
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
        playerCount=players.size();
    }

    public boolean matchBet (int playerSeatID){
        Player player = players.get(playerSeatID);
        int difference=minimumBet-pot.getBet(playerSeatID);
        if (player.getCashOnHand()>=difference){
            pot.add(difference,playerSeatID);
            player.setCashOnHand(player.getCashOnHand()-difference);
        } else {
            return false;
        }

        return true;
    }

    public boolean placeBet(int playerSeatID, int betAmount){
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

    public int addPlayer(long playerID){
        players.add(new Player(playerID));
        playerCount=players.size();
        lastGameActions.add(null);
        return players.size();
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public void nextTurn(){
        while(true) {
            presentTurn++;

            if (presentTurn == players.size()) {
                presentTurn = 0;
            }
            if (!players.get(presentTurn).getHasFolded()){
                break;
            }
        }
    }

    /**
     * returns the player matching the user ID
     * @param userID
     * @return
     */
    public Player getPlayer(long userID){
        for (Player p:players){
            if (p.getId()==userID){
                return p;
            }
        }
        return null;
    }

    /**
     * advances to the dealer to the next player
     */
    public void advanceDealer(){
        dealer++;
        if (dealer>=players.size()){
            dealer=0;
        }
    }

    @OneToMany(mappedBy = "gameState", cascade = CascadeType.ALL)
    @OrderColumn
    public List<GameAction> getLastGameActions() {
        return lastGameActions;
    }

    public void setLastGameActions(List<GameAction> lastGameActions) {
        this.lastGameActions = lastGameActions;
    }

    public void setLastGameAction(int playerID, GameAction action){
        lastGameActions.remove(playerID);
        lastGameActions.add(playerID,action);
    }
}
