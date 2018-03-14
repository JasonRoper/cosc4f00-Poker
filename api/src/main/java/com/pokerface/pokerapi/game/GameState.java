package com.pokerface.pokerapi.game;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GameState stores everything necessary to know about the state of a game.
 * It provides function to access that information.
 *
 * It exists within the GameRepository.
 */
@Entity
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
    private Card communityCardOne,communityCardTwo,communityCardThree,communityCardFour,communityCardFive;

    private int previousTurn;

    /**
     * These are game settings
     *
     */
    private int bigBlind;
    int minPlayerCount;
    int defaultCashOnHand;


    public GameState(){

    }

    /**
     * GameState constructory, with an ID, setting itself up with default settings.
     * @param id being used to create the game a long GameID
     */
    public GameState(long id) {
        this(id,12,4,200);
        setId(id);
    }

    /**
     * The chained constructor creates a gameState based on all settings given
     * @param id the id of the game
     * @param bigBlind the bigBlind amount
     * @param minPlayerCount the minimum player count before it starts
     * @param defaultCashOnHand the default cash on hand for a joining player
     */
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

    /**
     * This creates a gameState with a deck only.
     * @param deck the deck for the gameState
     */
    public GameState(Deck deck) {
        this.deck = deck;
    }

    /**
     * This gets the gameState ID
     * @return long ID of gameState
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    /**
     * Sets the ID of the gameState
     * @param id a long value
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getPot returns the GameState pot object
     * @return a Pot object
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    public Pot getPot() {
        return pot;
    }

    /**
     * Sets a GameState pot Object
     * @param pot the Pot to be set
     */
    public void setPot(Pot pot) {
        this.pot = pot;
    }

    /**
     * gets the Deck attached to the gameState
     * @return a Deck object
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    public Deck getDeck() {
        return deck;
    }

    /**
     * sets a new Deck to the gameState
     * @param deck Deck object to be set
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    /**
     * sets the lastBetter, the PlayerID of the last person to bet, used to determine round ending criteria
     * @param lastBet the lastBetters playerID
     */
    public void setLastBet(int lastBet) {
        this.lastBet = lastBet;
    }

    /**
     * sets who the dealer is
     * @param dealer an integer representing the PlayerID of the dealer
     */
    public void setDealer(int dealer) {
        this.dealer = dealer;
    }

    /**
     * updated the minimum bet to stay in the game in a given round
     * @param minimumBet integer of the minimumBet
     */
    public void setMinimumBet(int minimumBet) {
        this.minimumBet = minimumBet;
    }

    /**
     * setBigBlind sets the big blind setting of the game
     * @param bigBlind integer value of bigBlind
     */
    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    /**
     * Sets who turn it currently is
     * @param presentTurn int representing playerID of current turn
     */
    public void setPresentTurn(int presentTurn) {
        this.presentTurn = presentTurn;
    }


    /**
     * Sets what round is on, should be 1,2 or 3.
     * @param round the round it is as an int
     */
    public void setRound(int round) {
        this.round = round;
    }

    /**
     * Sets the minimum player count setting.
     * @param minPlayerCount int of the minimum players
     */
    public void setMinPlayerCount(int minPlayerCount) {
        this.minPlayerCount = minPlayerCount;
    }

    /**
     * Sets the default cash on hand setting of the GameState
     * @param defaultCashOnHand int representing default cash on hand of a new player.
     */
    public void setDefaultCashOnHand(int defaultCashOnHand) {
        this.defaultCashOnHand = defaultCashOnHand;
    }

    /**
     * getMinPlayerCount returns the integer of the minimum amount of player setting
     * @return the integer representing the minplayercount
     */
    public int getMinPlayerCount() {
        return minPlayerCount;
    }

    /**
     * get default cash on hand returns the amount a player starts with
     * @return int of cash on hand
     */
    public int getDefaultCashOnHand() {
        return defaultCashOnHand;
    }

    /**
     * getLastBet returns the playerID of who bet last
     * @return int playerID of who bet last
     */
    public int getLastBet() {
        return lastBet;
    }

    /**
     * getDealer returns the player ID of the dealer
     * @return int of the dealer
     */
    public int getDealer() {
        return dealer;
    }

    /**
     * getMinimumBet returns the current minimum bet to stay in the game
     * @return int of my minimum bet
     */
    public int getMinimumBet() {
        return minimumBet;
    }

    /**
     * getBigBlind returns the bigblind setting
     * @return the int of the big blind
     */
    public int getBigBlind() {
        return bigBlind;
    }

    /**
     * getPresentTurn returns the playerID of whose turn it currently is
     * @return int of playerID whose turn it is
     */
    public int getPresentTurn() {
        return presentTurn;
    }

    /**
     * getRound returns what round it is
     * @return int value of 1 2 or 3
     */
    public int getRound() {
        return round;
    }

    /**
     * returns a List of the players
     * @return List of the current players in the game
     */
    @OneToMany(mappedBy = "gameState", cascade = CascadeType.ALL)
    @OrderColumn
    public List<Player> getPlayers() {
        return players;
    }


    /**
     * setPlayers sets the players in a game
     * @param players being set into the game
     */
    public void setPlayers(List<Player> players) {
        this.players= players;
        playerCount=players.size();
    }

    /**
     * matchBet returns a boolean value of if the player can match the minimum bet, and then does so
     * @param playerSeatID of the better trying to match the current bet
     * @return boolean value representing of if it was successful
     */
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

    /**
     * placeBet places a bet
     * @param playerSeatID the player betting
     * @param betAmount the amount being bet
     * @return if it worked
     */
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

    /**
     * Adds a player to the game
     * @param playerID the ID of the player
     * @return the amount of players in the game as an int
     */
    public int addPlayer(long playerID){
        Player player;
        players.add(new Player(playerID));
        playerCount=players.size();
        player=players.get(playerCount-1);
        player.setCashOnHand(defaultCashOnHand);
        player.setTableSeatID(players.size()-1);
        lastGameActions.add(null);
        return players.size();
    }

    /**
     * returns the amount of players in the game
     * @return int player Count
     */
    public int getPlayerCount() {
        return playerCount;
    }

    /**
     * setPlayerCount sets the player count
     * @param playerCount integer value of how many players there are
     */
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    /**
     * nextTurn advances to the next players turn
     */
    public void nextTurn(){
        while(true) {
            previousTurn=presentTurn;
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
     * @param userID being searched
     * @return the Player object of the one matching the UserID
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
     * Returns a Player using a PlayerID
     * @param playerID the PlayerID being searched
     * @return Player object that was found
     */
    public Player getPlayer(int playerID){
        return players.get(playerID);
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

    /**
     * getLastGameActions returns the player mapped lists of GameAction
     * @return List of gameActions
     */
    @OneToMany(mappedBy = "gameState", cascade = CascadeType.ALL)
    @OrderColumn
    public List<GameAction> getLastGameActions() {
        return lastGameActions;
    }

    /**
     * Sets a list of gameActions
     * @param lastGameActions the gameActions to be set
     */
    public void setLastGameActions(List<GameAction> lastGameActions) {
        this.lastGameActions = lastGameActions;
    }

    /**
     * This writes a players last action to the list
     * @param playerID the player who did the action
     * @param action the action they performed
     */
    public void setLastGameAction(int playerID, GameAction action){
        lastGameActions.remove(playerID);
        lastGameActions.add(playerID,action);
    }

    /**
     * Creates a list of the communityCards to return them
     * @return a list of the communityCards
     */
    public List<Card> receiveCommunityCards() {
        List<Card> cards = new ArrayList<>();

        cards.add(getCommunityCardOne());
        cards.add(getCommunityCardTwo());
        cards.add(getCommunityCardThree());
        if (round <= 2) {
            cards.add(getCommunityCardFour());
        }
        if (round <= 3) {
            cards.add(getCommunityCardFive());
        }
        return cards;
    }

    /**
     * All Community Cards return the card position, one through to five
     * @return return the community card being requested
     */
    public Card getCommunityCardOne(){
        return communityCardOne;
    }
    public Card getCommunityCardTwo(){
        return communityCardTwo;
    }
    public Card getCommunityCardThree(){
        return communityCardThree;
    }
    public Card getCommunityCardFour(){
        return communityCardFour;
    }
    public Card getCommunityCardFive(){
        return communityCardFive;
    }

    /**
     * SetCommunityCard takes a card from the deck and places it in the first empty spot
     * @param communityCard the card to be set, it takes it from the deck if not provided
     */
    public void setCommunityCard(Card communityCard){
        if (communityCardOne==null){
            communityCardOne=deck.getCard();
        } else if (communityCardTwo==null){
            communityCardTwo=deck.getCard();
        } else if (communityCardThree==null){
            communityCardThree=deck.getCard();
        } else if (communityCardFour==null){
            communityCardFour=deck.getCard();
        } else if (communityCardFive==null){
            communityCardFive=deck.getCard();
        }
    }

    /**
     * Returns whose turn it was last
     * @return the int of the previous turn
     */
    public int getPreviousTurn() {
        return previousTurn;
    }

    /**
     * Allows to to set whose turn it was last
     * @param previousTurn the int setting the previous turn
     */
    public void setPreviousTurn(int previousTurn) {
        this.previousTurn = previousTurn;
    }

    /**
     * Sets a community card specifically, if needed. Same for all from One through to Five
     * @param communityCardOne being set, a Card type
     */
    public void setCommunityCardOne(Card communityCardOne) {
        this.communityCardOne = communityCardOne;
    }

    public void setCommunityCardTwo(Card communityCardTwo) {
        this.communityCardTwo = communityCardTwo;
    }

    public void setCommunityCardThree(Card communityCardThree) {
        this.communityCardThree = communityCardThree;
    }

    public void setCommunityCardFour(Card communityCardFour) {
        this.communityCardFour = communityCardFour;
    }

    public void setCommunityCardFive(Card communityCardFive) {
        this.communityCardFive = communityCardFive;
    }

    public boolean removePlayer(long userID){
        Player player=getPlayer(userID);
        if (player.getIsDealer()){
            advanceDealer();
        }
        if (getPresentTurn()==player.getTableSeatID()){
            nextTurn();
        }
        players.remove(player);
        return true;
    }

}
