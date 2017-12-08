package com.pokerface.pokerapi.game;

import java.util.Random;
import java.util.Stack;

public class Deck {
    //private Card[] CardList = Card.values();
    //private Stack<Card> cards = new Stack<>();
    public Deck(){
        initializeDeck();
    }
//
//    public Deck(Stack<Card> cards){
//      //  this.cards=cards;
//    }

    public Object dealCard(){
        return null; //cards.pop();
    }

    public void initializeDeck(){
        Random rand = new Random(System.currentTimeMillis());
        boolean[] placed = new boolean[52];
        for (int i=0;i<52;i++){
            while (true){
                int pos = rand.nextInt(52);
                if (!placed[pos]){
                    placed[pos]=true;
                    //cards.push(CardList[pos]);
                    break;
                }

            }
        }
    }
}
