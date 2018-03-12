package com.pokerface.pokerapi.game;

/**
 * HandTransport contains two cards, sent in the event of a newHand being dealt
 */
public class HandTransport {
  Card cardOne;
  Card cardTwo;

  public HandTransport() {
  }

  public HandTransport(Card cardOne, Card cardTwo){
    this.cardOne=cardOne;
    this.cardTwo=cardTwo;
  }

}
