package com.pokerface.pokerapi.game;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * GameAction is the form of the GameActions received from the FrontEnd. It contains a bet, 0 if irrelevent, and a type.
 * gameState and id are used only internally for storing previous actions.
 */
@Entity
@Table(name = "gameaction")
@JsonIgnoreProperties(value={"gameState","id"})
public class GameAction {
    private GameActionType type;
    private int bet;
    private Player player;
    private long id; // id to the database

    public GameAction(){

    }

    public GameAction(GameActionType actionType,int bet,Player player){
        this.player=player;
        this.id=player.getUserID();
        type=actionType;
        this.bet=bet;
    }

    public GameAction(long userID, GameActionType actionType,int bet){
        this.id=userID;
        type=actionType;
        this.bet=bet;
    }

    /**
     * This creates an GameAction, only done for copying received actions into lastAction in the GameState
     * @param type is the type of action, an enum of GameActionType
     * @param bet the amount being bet, irrelevant for some types
     */
    public GameAction(GameActionType type, int bet){
        this.type=type;
        this.bet=bet;
    }

    /**
     * Returns the type of action
     * @return GameActionType an enum
     */
    public GameActionType getType (){
        return type;
    }

    /**
     * setType sets the GameActionType
     * @param type an enum of GameActionType
     */
    public void setType(GameActionType type) {
        this.type = type;
    }

    /**
     * returns the amount bet, if such a value exists
     * @return integer value of bet
     */
    public int getBet () {
        return bet;
    }

    /**
     * setHet sets the bet, an integer value
     * @param bet an int
     */
    public void setBet(int bet){this.bet=bet;}

    @OneToOne
    @JoinColumn(name="player")
    public Player getPlayer() {
        return player;
    }

    /**
     * setGameState sets the gameState for storage purposes of lastAction
     * @param player the gameState being set
     */
    public void setPlayer(Player player) {
        this.player=player;
    }

    /**
     * returns the ID
     * @return long ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    /**
     * setID sets the ID
     * @param id a long value
     */
    public void setId(long id) {
        this.id = id;
    }
}
