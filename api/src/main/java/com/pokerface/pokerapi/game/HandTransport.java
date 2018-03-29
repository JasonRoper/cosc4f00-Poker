package com.pokerface.pokerapi.game;

/**
 * HandTransport contains two cards, sent in the event of a newHand being dealt
 */
public class HandTransport {
  private Card cardOne;
  private Card cardTwo;

  public HandTransport() {
  }

  public HandTransport(Card cardOne, Card cardTwo){
    this.cardOne=cardOne;
    this.cardTwo=cardTwo;
  }

  public HandTransport(Player player){
    try {
      this.cardOne = player.getCardOne();
      this.cardTwo = player.getCardTwo();
    } catch (NullPointerException n) {
      System.out.println();
    }
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
}
