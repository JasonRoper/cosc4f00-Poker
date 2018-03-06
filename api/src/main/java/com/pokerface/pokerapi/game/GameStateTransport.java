package com.pokerface.pokerapi.game;
///NOT AT ALL DONE
public class GameStateTransport {
    private int pot;
    private long nextId;

    public GameStateTransport() {
    }

    public void setPot(int pot){
        this.pot = pot;
    }
    public void setNextId(long nextId) {
        this.nextId = nextId;
    }

    public int getPot() {
        return pot;
    }

    public long getNextId() {
        return nextId;
    }
}
