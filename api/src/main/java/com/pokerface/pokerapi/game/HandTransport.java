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
