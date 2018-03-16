package com.pokerface.pokerapi.game;

import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class DeckTest {

    Deck deck;
    Card card;

    @Before
    public void setUp(){
        deck=new Deck();
    }

    /**
     * Checks to make sure the deck empties
     */
    @Test
    public void emptyDeck(){
        for (int i=0;i<52;i++){
            card=deck.dealCard();
        }
    }
}
