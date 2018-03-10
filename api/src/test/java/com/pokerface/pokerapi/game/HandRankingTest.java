package com.pokerface.pokerapi.game;

import org.junit.Test;
import org.springframework.data.util.Pair;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HandRankingTest {

    @Test
    public void testRoyalFlush() {
        TestCase[] testCases = new TestCase[]{
                new TestCase(new Card[]{Card.SPADES_JACK, Card.SPADES_ACE,
                        Card.SPADES_QUEEN, Card.SPADES_TEN, Card.SPADES_KING,
                        Card.DIAMONDS_QUEEN, Card.SPADES_SIX}, HandRanking.Type.ROYAL_FLUSH),

        };

        testRanking(testCases);
    }

    @Test
    public void testStraightFlush() {
        TestCase[] testCases = new TestCase[]{
                new TestCase(new Card[]{Card.HEARTS_ACE, Card.HEARTS_TWO,
                        Card.HEARTS_FIVE, Card.HEARTS_FOUR, Card.HEARTS_THREE,
                        Card.SPADES_SIX, Card.DIAMONDS_TWO}, HandRanking.Type.STRAIGHT_FLUSH),
        };

        testRanking(testCases);
    }

    @Test
    public void testFourOfAKind() {
        TestCase[] testCases = new TestCase[]{
                new TestCase(new Card[]{Card.DIAMONDS_SIX, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR, Card.SPADES_SIX, Card.HEARTS_SIX,
                        Card.SPADES_QUEEN, Card.CLUBS_SIX}, HandRanking.Type.FOUR_OF_A_KIND),
        };

        testRanking(testCases);
    }

    @Test
    public void testFullHouse() {
        TestCase[] testCases = new TestCase[]{
                new TestCase(new Card[]{Card.SPADES_JACK, Card.HEARTS_JACK,
                        Card.SPADES_QUEEN, Card.HEARTS_QUEEN, Card.DIAMONDS_QUEEN,
                        Card.DIAMONDS_ACE, Card.DIAMONDS_SEVEN}, HandRanking.Type.FULL_HOUSE),

        };

        testRanking(testCases);
    }

    @Test
    public void testFlush() {
        TestCase[] testCases = new TestCase[]{
                new TestCase(new Card[]{Card.HEARTS_ACE, Card.HEARTS_JACK,
                        Card.HEARTS_QUEEN, Card.HEARTS_FOUR, Card.HEARTS_THREE,
                        Card.SPADES_SIX, Card.DIAMONDS_TWO}, HandRanking.Type.FLUSH),

        };

        testRanking(testCases);
    }

    @Test
    public void testStraight() {
        TestCase[] testCases = new TestCase[]{
                new TestCase(new Card[]{Card.HEARTS_FIVE, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR, Card.SPADES_SIX, Card.HEARTS_THREE,
                        Card.SPADES_QUEEN, Card.CLUBS_FIVE}, HandRanking.Type.STRAIGHT),

                // there is a special case where if the high card is the last element
                // of the straight, there may be an issue.
                new TestCase(new Card[]{Card.HEARTS_FIVE, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR, Card.SPADES_SIX, Card.HEARTS_THREE,
                        Card.SPADES_ACE, Card.CLUBS_FIVE}, HandRanking.Type.STRAIGHT),

                // need to check the wrap around on KING -> ACE
                new TestCase(new Card[]{Card.HEARTS_FIVE, Card.DIAMONDS_TWO,
                        Card.CLUBS_TEN, Card.HEARTS_JACK, Card.DIAMONDS_QUEEN,
                        Card.SPADES_KING, Card.CLUBS_ACE}, HandRanking.Type.STRAIGHT),
        };

        testRanking(testCases);
    }

    @Test
    public void testThreeOfAKind() {
        TestCase[] testCases = new TestCase[]{
                new TestCase(new Card[]{Card.HEARTS_SIX, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR, Card.SPADES_SIX, Card.HEARTS_ACE,
                        Card.SPADES_QUEEN, Card.CLUBS_SIX}, HandRanking.Type.THREE_OF_A_KIND),

        };

        testRanking(testCases);
    }

    @Test
    public void testTwoPair() {
        TestCase[] testCases = new TestCase[]{
                new TestCase(new Card[]{Card.HEARTS_SEVEN, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR, Card.SPADES_SIX, Card.HEARTS_ACE,
                        Card.SPADES_FOUR, Card.CLUBS_SIX}, HandRanking.Type.TWO_PAIR),
        };

        testRanking(testCases);
    }

    @Test
    public void testOnePair() {
        TestCase[] testCases = new TestCase[]{
                new TestCase(new Card[]{Card.HEARTS_SEVEN, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR, Card.SPADES_SIX, Card.HEARTS_ACE,
                        Card.SPADES_FOUR, Card.SPADES_KING}, HandRanking.Type.ONE_PAIR),

        };

        testRanking(testCases);
    }

    @Test
    public void testHighCard() {
        TestCase[] testCases = new TestCase[]{
                new TestCase(new Card[]{Card.HEARTS_SEVEN, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR, Card.SPADES_SIX, Card.HEARTS_ACE,
                        Card.SPADES_NINE, Card.SPADES_KING}, HandRanking.Type.HIGH_CARD),
        };

        testRanking(testCases);
    }

    public void testRanking(TestCase[] testCases) {
        for (TestCase testCase : testCases) {
            HandRanking ranking = new HandRanking(Arrays.asList(testCase.hand));

            assertEquals("Failed to correctly rank " + testCase.result.toString(),
                    testCase.result, ranking.getHandType());
        }
    }

    @Test
    public void testStraightFlushTieBreaker() {
        Pair[] testCases = new Pair[]{
                Pair.of(
                        new Card[]{
                                Card.CLUBS_KING, Card.CLUBS_QUEEN, Card.CLUBS_JACK, Card.CLUBS_TEN,
                                Card.CLUBS_NINE, Card.CLUBS_FIVE, Card.SPADES_TEN
                        },
                        new Card[]{
                                Card.HEARTS_SIX, Card.HEARTS_TWO, Card.HEARTS_THREE, Card.HEARTS_FOUR,
                                Card.HEARTS_FIVE, Card.DIAMONDS_FIVE, Card.SPADES_SIX
                        })
        };

        for (Pair<Card[], Card[]> testCase : testCases) {
            testTieBreaker(HandRanking.Type.STRAIGHT_FLUSH, testCase.getFirst(), testCase.getSecond());
        }
    }

    @Test
    public void testFourOfAKindTieBreaker() {
        Pair[] testCases = new Pair[]{
                Pair.of(
                        new Card[]{
                                Card.DIAMONDS_QUEEN, Card.HEARTS_ACE, Card.HEARTS_THREE, Card.HEARTS_FOUR,
                                Card.HEARTS_ACE, Card.DIAMONDS_ACE, Card.CLUBS_ACE
                        },
                        new Card[]{
                                Card.CLUBS_QUEEN, Card.HEARTS_ACE, Card.CLUBS_QUEEN, Card.HEARTS_QUEEN,
                                Card.DIAMONDS_QUEEN, Card.CLUBS_FIVE, Card.SPADES_TEN
                        }),
                Pair.of(
                        new Card[]{
                                Card.DIAMONDS_KING, Card.HEARTS_ACE, Card.HEARTS_THREE, Card.HEARTS_FOUR,
                                Card.HEARTS_ACE, Card.DIAMONDS_ACE, Card.CLUBS_ACE
                        },
                        new Card[]{
                                Card.CLUBS_QUEEN, Card.HEARTS_ACE, Card.CLUBS_THREE, Card.HEARTS_TWO,
                                Card.DIAMONDS_ACE, Card.CLUBS_ACE, Card.SPADES_ACE
                        })
        };

        for (Pair<Card[], Card[]> testCase : testCases) {
            testTieBreaker(HandRanking.Type.FOUR_OF_A_KIND, testCase.getFirst(), testCase.getSecond());
        }
    }

    @Test
    public void testFullHouseTieBreaker() {
        Pair[] testCases = new Pair[]{
                Pair.of(
                        new Card[]{
                                Card.DIAMONDS_QUEEN, Card.HEARTS_ACE, Card.HEARTS_THREE, Card.HEARTS_FOUR,
                                Card.HEARTS_QUEEN, Card.DIAMONDS_ACE, Card.CLUBS_ACE
                        },
                        new Card[]{
                                Card.CLUBS_ACE, Card.HEARTS_ACE, Card.CLUBS_QUEEN, Card.HEARTS_QUEEN,
                                Card.DIAMONDS_QUEEN, Card.CLUBS_FIVE, Card.SPADES_TEN
                        })
        };

        for (Pair<Card[], Card[]> testCase : testCases) {
            testTieBreaker(HandRanking.Type.FULL_HOUSE, testCase.getFirst(), testCase.getSecond());
        }
    }

    @Test
    public void testFlushTieBreaker() {
        Pair[] testCases = new Pair[]{
                Pair.of(
                        new Card[]{
                                Card.CLUBS_ACE, Card.CLUBS_JACK, Card.CLUBS_QUEEN, Card.CLUBS_KING,
                                Card.HEARTS_FOUR, Card.CLUBS_FIVE, Card.SPADES_TEN
                        },
                        new Card[]{
                                Card.HEARTS_ACE, Card.HEARTS_TWO, Card.HEARTS_THREE, Card.HEARTS_FOUR,
                                Card.HEARTS_TEN, Card.DIAMONDS_FIVE, Card.SPADES_SIX
                        })
        };

        for (Pair<Card[], Card[]> testCase : testCases) {
            testTieBreaker(HandRanking.Type.FLUSH, testCase.getFirst(), testCase.getSecond());
        }
    }

    @Test
    public void testStraightTieBreaker() {
        Pair[] testCases = new Pair[]{
                Pair.of(
                        new Card[]{
                                Card.CLUBS_ACE, Card.HEARTS_JACK, Card.SPADES_QUEEN, Card.DIAMONDS_KING,
                                Card.HEARTS_FOUR, Card.DIAMONDS_FIVE, Card.SPADES_TEN
                        },
                        new Card[]{
                                Card.CLUBS_ACE, Card.HEARTS_TWO, Card.SPADES_THREE, Card.DIAMONDS_FOUR,
                                Card.HEARTS_FOUR, Card.DIAMONDS_FIVE, Card.SPADES_SIX
                        })
        };

        for (Pair<Card[], Card[]> testCase : testCases) {
            testTieBreaker(HandRanking.Type.STRAIGHT, testCase.getFirst(), testCase.getSecond());
        }
    }

    @Test
    public void testThreeOfAKindTieBreaker() {
        Pair[] testCases = new Pair[]{
                Pair.of(
                        new Card[]{
                                Card.CLUBS_ACE, Card.HEARTS_ACE, Card.SPADES_ACE, Card.DIAMONDS_QUEEN,
                                Card.HEARTS_FOUR, Card.DIAMONDS_SEVEN, Card.SPADES_SIX
                        },
                        new Card[]{
                                Card.CLUBS_THREE, Card.HEARTS_THREE, Card.SPADES_THREE, Card.DIAMONDS_NINE,
                                Card.HEARTS_TWO, Card.DIAMONDS_FOUR, Card.SPADES_SEVEN
                        }),
                Pair.of(
                        new Card[]{
                                Card.CLUBS_JACK, Card.HEARTS_JACK, Card.SPADES_JACK, Card.DIAMONDS_QUEEN,
                                Card.HEARTS_TWO, Card.DIAMONDS_THREE, Card.SPADES_SEVEN
                        },
                        new Card[]{
                                Card.CLUBS_JACK, Card.HEARTS_JACK, Card.SPADES_JACK, Card.DIAMONDS_TEN,
                                Card.HEARTS_TWO, Card.DIAMONDS_THREE, Card.SPADES_SIX
                        })
        };

        for (Pair<Card[], Card[]> testCase : testCases) {
            testTieBreaker(HandRanking.Type.THREE_OF_A_KIND, testCase.getFirst(), testCase.getSecond());
        }
    }

    @Test
    public void testTwoPairTieBreaker() {
        Pair[] testCases = new Pair[]{
                Pair.of(
                        new Card[]{
                                Card.CLUBS_ACE, Card.HEARTS_ACE, Card.SPADES_NINE, Card.DIAMONDS_QUEEN,
                                Card.HEARTS_FOUR, Card.DIAMONDS_FOUR, Card.SPADES_SIX
                        },
                        new Card[]{
                                Card.CLUBS_THREE, Card.HEARTS_THREE, Card.SPADES_NINE, Card.DIAMONDS_NINE,
                                Card.HEARTS_TWO, Card.DIAMONDS_FOUR, Card.SPADES_SEVEN
                        }),
                Pair.of(
                        new Card[]{
                                Card.CLUBS_ACE, Card.HEARTS_JACK, Card.SPADES_NINE, Card.DIAMONDS_JACK,
                                Card.HEARTS_TWO, Card.DIAMONDS_TWO, Card.SPADES_SIX
                        },
                        new Card[]{
                                Card.CLUBS_JACK, Card.HEARTS_JACK, Card.SPADES_NINE, Card.DIAMONDS_QUEEN,
                                Card.HEARTS_TWO, Card.DIAMONDS_TWO, Card.SPADES_SEVEN
                        })
        };

        for (Pair<Card[], Card[]> testCase : testCases) {
            testTieBreaker(HandRanking.Type.TWO_PAIR, testCase.getFirst(), testCase.getSecond());
        }
    }

    @Test
    public void testOnePairTieBreaker() {
        Pair[] testCases = new Pair[]{
                Pair.of(
                        new Card[]{
                                Card.CLUBS_JACK, Card.HEARTS_JACK, Card.SPADES_NINE, Card.DIAMONDS_QUEEN,
                                Card.HEARTS_TWO, Card.DIAMONDS_FOUR, Card.SPADES_SIX
                        },
                        new Card[]{
                                Card.CLUBS_THREE, Card.HEARTS_THREE, Card.SPADES_NINE, Card.DIAMONDS_QUEEN,
                                Card.HEARTS_TWO, Card.DIAMONDS_FOUR, Card.SPADES_SEVEN
                        }),
                Pair.of(
                        new Card[]{
                                Card.CLUBS_ACE, Card.HEARTS_ACE, Card.SPADES_NINE, Card.DIAMONDS_QUEEN,
                                Card.HEARTS_TWO, Card.DIAMONDS_FOUR, Card.SPADES_SIX
                        },
                        new Card[]{
                                Card.CLUBS_ACE, Card.HEARTS_JACK, Card.SPADES_NINE, Card.DIAMONDS_QUEEN,
                                Card.HEARTS_TWO, Card.DIAMONDS_JACK, Card.SPADES_SEVEN
                        }),
                Pair.of(
                        new Card[]{
                                Card.CLUBS_ACE, Card.HEARTS_ACE, Card.SPADES_NINE, Card.DIAMONDS_QUEEN,
                                Card.HEARTS_TWO, Card.DIAMONDS_JACK, Card.SPADES_SIX
                        },
                        new Card[]{
                                Card.CLUBS_ACE, Card.HEARTS_ACE, Card.SPADES_NINE, Card.DIAMONDS_QUEEN,
                                Card.HEARTS_TWO, Card.DIAMONDS_FOUR, Card.SPADES_SEVEN
                        })
        };

        for (Pair<Card[], Card[]> testCase : testCases) {
            testTieBreaker(HandRanking.Type.ONE_PAIR, testCase.getFirst(), testCase.getSecond());
        }
    }

    @Test
    public void testHighCardTieBreaker() {
        Pair[] testCases = new Pair[]{
                Pair.of(

                        new Card[]{
                                Card.CLUBS_ACE, Card.HEARTS_JACK, Card.SPADES_NINE, Card.DIAMONDS_QUEEN,
                                Card.HEARTS_TWO, Card.DIAMONDS_FOUR, Card.SPADES_SEVEN
                        },
                        new Card[]{
                                Card.CLUBS_THREE, Card.HEARTS_JACK, Card.SPADES_NINE, Card.DIAMONDS_QUEEN,
                                Card.HEARTS_TWO, Card.DIAMONDS_FOUR, Card.SPADES_SIX
                        }),
                Pair.of(
                        new Card[]{
                                Card.CLUBS_ACE, Card.HEARTS_JACK, Card.SPADES_NINE, Card.DIAMONDS_QUEEN,
                                Card.HEARTS_TWO, Card.DIAMONDS_FIVE, Card.SPADES_SEVEN
                        },
                        new Card[]{
                                Card.CLUBS_ACE, Card.HEARTS_JACK, Card.SPADES_NINE, Card.DIAMONDS_QUEEN,
                                Card.HEARTS_TWO, Card.DIAMONDS_FOUR, Card.SPADES_SIX
                        })
        };

        for (Pair<Card[], Card[]> testCase : testCases) {
            testTieBreaker(HandRanking.Type.HIGH_CARD, testCase.getFirst(), testCase.getSecond());
        }
    }

    public void testTieBreaker(HandRanking.Type type, Card[] winner, Card[] looser) {
        HandRanking winnerRanking = new HandRanking(Arrays.asList(winner));
        HandRanking looserRanking = new HandRanking(Arrays.asList(looser));

        assertEquals("winner ranking incorrect", type, winnerRanking.getHandType());
        assertEquals("looser ranking incorrect", type, looserRanking.getHandType());

        assertTrue("winner ranked below looser", winnerRanking.compareTo(looserRanking) == 1);
        assertTrue("looser ranked above winner", looserRanking.compareTo(winnerRanking) == -1);
        assertTrue("winner didn't tie with themselves", winnerRanking.compareTo(winnerRanking) == 0);
        assertTrue("looser didn't tie with themselves", looserRanking.compareTo(looserRanking) == 0);
    }

    @Test
    public void testLessThanSevenCards() {
        HandRanking ranking = new HandRanking(Arrays.asList(Card.SPADES_KING));
        assertEquals(HandRanking.Type.HIGH_CARD, ranking.getHandType());
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
