package com.pokerface.pokerapi.game;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GameState {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    long id;

    int pot;
    long nextTurnId;

    public GameState(){

    }

    public GameState(long id) {
        this.id = id;
    }

    public void play(GameAction action) {
        nextTurnId++;
    }

    public GameTransport toTransport() {
        return new GameTransport();
    }
}
