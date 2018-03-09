package com.pokerface.pokerapi.game;

import com.pokerface.pokerapi.util.TestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CardsTest {

    @Test
    public void testNext(){
        TestCase[] testCases = new TestCase[]{
                new TestCase<Card,Card>("two to three", Card.CLUBS_TWO, Card.CLUBS_THREE),
                new TestCase<Card,Card>("king to ace", Card.CLUBS_KING, Card.CLUBS_ACE),
                new TestCase<Card,Card>("ace to null", Card.CLUBS_ACE, Card.CLUBS_TWO),
        };

        for (TestCase<Card,Card> testCase : testCases) {
            assertEquals(testCase.getMessage(), testCase.getCorrectResult(), testCase.getInput().next());
        }
    }

    @Test
    public void testRank(){
        TestCase[] testCases = new TestCase[]{
                new TestCase<Card,Card.Rank>(Card.CLUBS_SIX, Card.Rank.SIX),
                new TestCase<Card,Card.Rank>(Card.CLUBS_ACE, Card.Rank.ACE),
                new TestCase<Card,Card.Rank>(Card.SPADES_ACE, Card.Rank.ACE),
                new TestCase<Card,Card.Rank>(Card.CLUBS_KING, Card.Rank.KING)
        };

        for (TestCase<Card,Card.Rank> testCase : testCases) {
            assertEquals(testCase.getMessage(), testCase.getCorrectResult(), testCase.getInput().rank());
        }
    }

    @Test
    public void testSuit(){
        TestCase[] testCases = new TestCase[]{
                new TestCase<Card,Card.Suit>(Card.CLUBS_SIX, Card.Suit.CLUBS),
                new TestCase<Card,Card.Suit>(Card.HEARTS_ACE, Card.Suit.HEARTS),
                new TestCase<Card,Card.Suit>(Card.SPADES_ACE, Card.Suit.SPADES),
                new TestCase<Card,Card.Suit>(Card.DIAMONDS_KING, Card.Suit.DIAMONDS)
        };

        for (TestCase<Card,Card.Suit> testCase : testCases) {
            assertEquals(testCase.getMessage(), testCase.getCorrectResult(), testCase.getInput().suit());
        }
    }
}
