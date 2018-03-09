package com.pokerface.pokerapi.game;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class HandRankingTest {

    @Test
    public void testHandRanking() {
        TestCase[] testCases = new TestCase[]{
                new TestCase(new Card[]{Card.SPADES_JACK, Card.SPADES_ACE,
                        Card.SPADES_QUEEN,Card.SPADES_TEN, Card.SPADES_KING,
                        Card.DIAMONDS_QUEEN, Card.SPADES_SIX}, HandRanking.Type.ROYAL_FLUSH),
                new TestCase(new Card[]{Card.SPADES_JACK, Card.HEARTS_JACK,
                        Card.SPADES_QUEEN,Card.HEARTS_QUEEN, Card.DIAMONDS_QUEEN,
                        Card.DIAMONDS_ACE, Card.DIAMONDS_SEVEN}, HandRanking.Type.FULL_HOUSE),
                new TestCase(new Card[]{Card.HEARTS_ACE, Card.HEARTS_TWO,
                        Card.HEARTS_FIVE,Card.HEARTS_FOUR, Card.HEARTS_THREE,
                        Card.SPADES_SIX, Card.DIAMONDS_TWO}, HandRanking.Type.STRAIGHT_FLUSH),
                new TestCase(new Card[]{Card.HEARTS_ACE, Card.HEARTS_JACK,
                        Card.HEARTS_QUEEN,Card.HEARTS_FOUR, Card.HEARTS_THREE,
                        Card.SPADES_SIX, Card.DIAMONDS_TWO}, HandRanking.Type.FLUSH),
                new TestCase(new Card[]{Card.HEARTS_FIVE, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR,Card.SPADES_SIX, Card.HEARTS_THREE,
                        Card.SPADES_QUEEN, Card.CLUBS_FIVE}, HandRanking.Type.STRAIGHT),

                // there is a special case where if the high card is the last element
                // of the straight, there may be an issue.
                new TestCase(new Card[]{Card.HEARTS_FIVE, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR,Card.SPADES_SIX, Card.HEARTS_THREE,
                        Card.SPADES_ACE, Card.CLUBS_FIVE}, HandRanking.Type.STRAIGHT),
                new TestCase(new Card[]{Card.DIAMONDS_SIX, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR,Card.SPADES_SIX, Card.HEARTS_SIX,
                        Card.SPADES_QUEEN, Card.CLUBS_SIX}, HandRanking.Type.FOUR_OF_A_KIND),
                new TestCase(new Card[]{Card.HEARTS_SIX, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR,Card.SPADES_SIX, Card.HEARTS_ACE,
                        Card.SPADES_QUEEN, Card.CLUBS_SIX}, HandRanking.Type.THREE_OF_A_KIND),
                new TestCase(new Card[]{Card.HEARTS_SEVEN, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR,Card.SPADES_SIX, Card.HEARTS_ACE,
                        Card.SPADES_FOUR, Card.CLUBS_SIX}, HandRanking.Type.TWO_PAIR),
                new TestCase(new Card[]{Card.HEARTS_SEVEN, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR,Card.SPADES_SIX, Card.HEARTS_ACE,
                        Card.SPADES_FOUR, Card.SPADES_KING}, HandRanking.Type.ONE_PAIR),
                new TestCase(new Card[]{Card.HEARTS_SEVEN, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR,Card.SPADES_SIX, Card.HEARTS_ACE,
                        Card.SPADES_NINE, Card.SPADES_KING}, HandRanking.Type.HIGH_CARD),
        };

        for (TestCase testCase : testCases) {
            HandRanking ranking = new HandRanking(Arrays.asList(testCase.hand));
            assertEquals("Failed to correctly rank " + testCase.result.toString(),
                    testCase.result, ranking.getRank());
        }
    }

    public class TestCase {
        Card[] hand;
        HandRanking.Type result;

        public TestCase(Card[] hand, HandRanking.Type result) {
            this.hand = hand;
            this.result = result;
        }
    }
}
