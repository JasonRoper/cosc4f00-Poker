package com.pokerface.pokerapi.game;

public class GameTransport {  
  private int pot;
  private long nextId;

  public GameTransport() {    
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
