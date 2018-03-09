package com.pokerface.pokerapi.game;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "gameaction")
@JsonIgnoreProperties(value={"gameState","id"})
public class GameAction {
    private GameActionType type;
    private int bet;
    private GameState gameState;
    private long id; // id to the database

    public GameAction(){

    }

    public GameAction(GameActionType type, int bet){
        this.type=type;
        this.bet=bet;
    }

    public GameActionType getType (){
        return type;
    }

    public void setType(GameActionType type) {
        this.type = type;
    }

    public int getBet () {
        return bet;
    }

    public void setBet(int bet){this.bet=bet;}

    @ManyToOne
    @JoinColumn(name="gameState")
    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
