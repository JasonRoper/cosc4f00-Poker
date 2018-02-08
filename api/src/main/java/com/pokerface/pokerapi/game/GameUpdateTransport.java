package com.pokerface.pokerapi.game;
//NOT DONE AT ALL
public class GameUpdateTransport {
  private int pot;
  private long nextId;

  public GameUpdateTransport() {
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
