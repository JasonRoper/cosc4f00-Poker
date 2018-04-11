package com.pokerface.pokerapi.game;

import com.pokerface.pokerapi.util.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * {@link HandRankingTest} checks to see if {@link HandRanking} is ranking hands correctly.
 */
public class HandRankingTest {

    /**
     * Test to see if a Royal flush is correctly identified by {@link HandRanking}
     */
    @Test
    public void testRoyalFlush() {
        TestCase[] testCases = new TestCase[]{
                new TestCase<>(new Card[]{Card.SPADES_JACK, Card.SPADES_ACE,
                        Card.SPADES_QUEEN, Card.SPADES_TEN, Card.SPADES_KING,
                        Card.DIAMONDS_QUEEN, Card.SPADES_SIX}, HandRanking.Type.ROYAL_FLUSH),

        };

        testRanking(testCases);
    }

    /**
     * Test to see if a straight flush is correctly identified by {@link HandRanking}
     */
    @Test
    public void testStraightFlush() {
        TestCase[] testCases = new TestCase[]{
                new TestCase<>(new Card[]{Card.HEARTS_ACE, Card.HEARTS_TWO,
                        Card.HEARTS_FIVE, Card.HEARTS_FOUR, Card.HEARTS_THREE,
                        Card.SPADES_SIX, Card.DIAMONDS_TWO}, HandRanking.Type.STRAIGHT_FLUSH),
        };

        testRanking(testCases);
    }

    /**
     * Test to see if a four of a kind is correctly identified by {@link HandRanking}
     */
    @Test
    public void testFourOfAKind() {
        TestCase[] testCases = new TestCase[]{
                new TestCase<>(new Card[]{Card.DIAMONDS_SIX, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR, Card.SPADES_SIX, Card.HEARTS_SIX,
                        Card.SPADES_QUEEN, Card.CLUBS_SIX}, HandRanking.Type.FOUR_OF_A_KIND),
        };

        testRanking(testCases);
    }

    /**
     * Test to see if a full house is correctly identified by {@link HandRanking}
     */
    @Test
    public void testFullHouse() {
        TestCase[] testCases = new TestCase[]{
                new TestCase<>(new Card[]{Card.SPADES_JACK, Card.HEARTS_JACK,
                        Card.SPADES_QUEEN, Card.HEARTS_QUEEN, Card.DIAMONDS_QUEEN,
                        Card.DIAMONDS_ACE, Card.DIAMONDS_SEVEN}, HandRanking.Type.FULL_HOUSE),

        };

        testRanking(testCases);
    }

    /**
     * Test to see if a flush is correctly identified by {@link HandRanking}
     */
    @Test
    public void testFlush() {
        TestCase[] testCases = new TestCase[]{
                new TestCase<>(new Card[]{Card.HEARTS_ACE, Card.HEARTS_JACK,
                        Card.HEARTS_QUEEN, Card.HEARTS_FOUR, Card.HEARTS_THREE,
                        Card.SPADES_SIX, Card.DIAMONDS_TWO}, HandRanking.Type.FLUSH),

        };

        testRanking(testCases);
    }

    /**
     * Test to see if a straight is correctly identified by {@link HandRanking}
     */
    @Test
    public void testStraight() {
        TestCase[] testCases = new TestCase[]{
                new TestCase<>(new Card[]{Card.HEARTS_FIVE, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR, Card.SPADES_SIX, Card.HEARTS_THREE,
                        Card.SPADES_QUEEN, Card.CLUBS_FIVE}, HandRanking.Type.STRAIGHT),

                // there is a special case where if the high card is the last element
                // of the straight, there may be an issue.
                new TestCase<>(new Card[]{Card.HEARTS_FIVE, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR, Card.SPADES_SIX, Card.HEARTS_THREE,
                        Card.SPADES_ACE, Card.CLUBS_FIVE}, HandRanking.Type.STRAIGHT),

                // need to check the wrap around on KING -> ACE
                new TestCase<>(new Card[]{Card.HEARTS_FIVE, Card.DIAMONDS_TWO,
                        Card.CLUBS_TEN, Card.HEARTS_JACK, Card.DIAMONDS_QUEEN,
                        Card.SPADES_KING, Card.CLUBS_ACE}, HandRanking.Type.STRAIGHT),
        };

        testRanking(testCases);
    }

    /**
     * Test to see if a three of a kind is correctly identified by {@link HandRanking}
     */
    @Test
    public void testThreeOfAKind() {
        TestCase[] testCases = new TestCase[]{
                new TestCase<>(new Card[]{Card.HEARTS_SIX, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR, Card.SPADES_SIX, Card.HEARTS_ACE,
                        Card.SPADES_QUEEN, Card.CLUBS_SIX}, HandRanking.Type.THREE_OF_A_KIND),

        };

        testRanking(testCases);
    }

    /**
     * Test to see if two pair is correctly identified by {@link HandRanking}
     */
    @Test
    public void testTwoPair() {
        TestCase[] testCases = new TestCase[]{
                new TestCase<>(new Card[]{Card.HEARTS_SEVEN, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR, Card.SPADES_SIX, Card.HEARTS_ACE,
                        Card.SPADES_FOUR, Card.CLUBS_SIX}, HandRanking.Type.TWO_PAIR),
        };

        testRanking(testCases);
    }

    /**
     * test to see if one pair is correctly identified by {@link HandRanking}
     */
    @Test
    public void testOnePair() {
        TestCase[] testCases = new TestCase[]{
                new TestCase<>(new Card[]{Card.HEARTS_SEVEN, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR, Card.SPADES_SIX, Card.HEARTS_ACE,
                        Card.SPADES_FOUR, Card.SPADES_KING}, HandRanking.Type.ONE_PAIR),

        };

        testRanking(testCases);
    }

    /**
     * Test to see if high card hand is correctly identified by {@link HandRanking}
     */
    @Test
    public void testHighCard() {
        TestCase[] testCases = new TestCase[]{
                new TestCase<>(new Card[]{Card.HEARTS_SEVEN, Card.DIAMONDS_TWO,
                        Card.DIAMONDS_FOUR, Card.SPADES_SIX, Card.HEARTS_ACE,
                        Card.SPADES_NINE, Card.SPADES_KING}, HandRanking.Type.HIGH_CARD),
        };

        testRanking(testCases);
    }

    /**
     * Test a list of TestCases containing a list of {@link Card}s, and the expected {@link HandRanking.Type}
     * that the hand should be ranked as
     * @param testCases the testCases to test
     */
    private void testRanking(TestCase<Card[],HandRanking.Type>[] testCases) {
        for (TestCase<Card[],HandRanking.Type> testCase : testCases) {
            HandRanking ranking = new HandRanking(Arrays.asList(testCase.getInput()));

            assertEquals("Failed to correctly rank " + testCase.getCorrectResult().toString(),
                    testCase.getCorrectResult(), ranking.getHandType());
        }
    }

    /**
     * Test to see if two hands contain a straight flush, the straight flush with the high card wins.
     */
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

    /**
     * Test to see if two hands contain a four of a kind, the hand with the highest four of a kind wins,
     * and if they are the same (in the event of the four of a kind being in the community cards) the
     * remaining high card is then compared, otherwise, tie.
     */
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

    /**
     * Test to see if two hands contain a full house, the hand with the highest 3 of a kind is wins, and
     * if it ties, then the hand with with the highest pair wins, otherwise, tie.
     */
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

    /**
     * Test to see that if two hand contain a flush, the flush with the highest cards wins.
     */
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

    /**
     * Test to see that if two hands contain a straight, the straight with the highest card wins.
     */
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

    /**
     * Test to see that if two hands contain a 3 of a kind, the hand that contains the highest 3 of a kind
     * wins, and if they tie, then each hand is ranked by high card twice, otherwise, tie.
     */
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

    /**
     * Test to see that if two hands contain two pairs, the hand that contains a higher pair wins. If both
     * hand have the same pairs, the hand with the highest remaining card wins, otherwise, tie.
     */
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

    /**
     * Test to see that if two hands contain a pair, the hand that contains the higher pair wins. If both
     * hands have the same pair, the player with the highest card wins, if they tie, tie break by high card
     * two more times, then tie.
     */
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

    /**
     * Test to see that if two hands do not contain any hand, they are ranked by high card 5 times, otherwise
     * tie.
     */
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

    /**
     * find out if given two hands, that they are the same hand (of {@code type}), and that the winner hand
     * is always ranked above the looser hand.
     *
     * @param type the type of hand that both winner and looser are expected to be
     * @param winner the hand that should win among the two hands
     * @param looser the hand that should loose among the two hands
     */
    private void testTieBreaker(HandRanking.Type type, Card[] winner, Card[] looser) {
        HandRanking winnerRanking = new HandRanking(Arrays.asList(winner));
        HandRanking looserRanking = new HandRanking(Arrays.asList(looser));

        assertEquals("winner ranking incorrect", type, winnerRanking.getHandType());
        assertEquals("looser ranking incorrect", type, looserRanking.getHandType());

        assertTrue("winner ranked below looser", winnerRanking.compareTo(looserRanking) == 1);
        assertTrue("looser ranked above winner", looserRanking.compareTo(winnerRanking) == -1);
        assertTrue("winner didn't tie with themselves", winnerRanking.compareTo(winnerRanking) == 0);
        assertTrue("looser didn't tie with themselves", looserRanking.compareTo(looserRanking) == 0);
    }

    /**
     * Test to see if {@link HandRanking} functions when given only one card
     */
    @Test
    public void testLessThanSevenCards() {
        HandRanking ranking = new HandRanking(Arrays.asList(Card.SPADES_KING));
        assertEquals(HandRanking.Type.HIGH_CARD, ranking.getHandType());
    }

    @Test
    public void testHandComparisons() {
        TestCase[] rankings = new TestCase[]{
                new TestCase<>(new HandRanking(Arrays.asList(Card.CLUBS_ACE, Card.CLUBS_KING, Card.CLUBS_QUEEN, Card.CLUBS_JACK, Card.CLUBS_TEN, Card.DIAMONDS_TEN, Card.DIAMONDS_SEVEN)),
                        HandRanking.Type.ROYAL_FLUSH),
                new TestCase<>(new HandRanking(Arrays.asList(Card.DIAMONDS_NINE, Card.DIAMONDS_EIGHT, Card.DIAMONDS_SIX, Card.CLUBS_JACK, Card.CLUBS_TEN, Card.DIAMONDS_TEN, Card.DIAMONDS_SEVEN)),
                        HandRanking.Type.STRAIGHT_FLUSH),
                new TestCase<>(new HandRanking(Arrays.asList(Card.CLUBS_ACE, Card.HEARTS_ACE, Card.SPADES_ACE, Card.DIAMONDS_ACE, Card.CLUBS_TEN, Card.DIAMONDS_TEN, Card.DIAMONDS_SEVEN)),
                        HandRanking.Type.FOUR_OF_A_KIND),
                new TestCase<>(new HandRanking(Arrays.asList(Card.CLUBS_ACE, Card.HEARTS_ACE, Card.SPADES_ACE, Card.SPADES_JACK, Card.CLUBS_TEN, Card.DIAMONDS_TEN, Card.DIAMONDS_SEVEN)),
                        HandRanking.Type.FULL_HOUSE),
                new TestCase<>(new HandRanking(Arrays.asList(Card.CLUBS_ACE, Card.SPADES_TWO, Card.DIAMONDS_TWO, Card.DIAMONDS_ACE, Card.DIAMONDS_EIGHT, Card.DIAMONDS_TEN, Card.DIAMONDS_SEVEN)),
                        HandRanking.Type.FLUSH),
                new TestCase<>(new HandRanking(Arrays.asList(Card.CLUBS_ACE, Card.HEARTS_ACE, Card.HEARTS_JACK, Card.SPADES_NINE, Card.CLUBS_EIGHT, Card.DIAMONDS_TEN, Card.DIAMONDS_SEVEN)),
                        HandRanking.Type.STRAIGHT),
                new TestCase<>(new HandRanking(Arrays.asList(Card.CLUBS_ACE, Card.HEARTS_ACE, Card.SPADES_ACE, Card.SPADES_FOUR, Card.CLUBS_TEN, Card.DIAMONDS_FIVE, Card.DIAMONDS_SEVEN)),
                        HandRanking.Type.THREE_OF_A_KIND),
                new TestCase<>(new HandRanking(Arrays.asList(Card.CLUBS_ACE, Card.HEARTS_ACE, Card.SPADES_JACK, Card.DIAMONDS_JACK, Card.CLUBS_TEN, Card.DIAMONDS_FIVE, Card.DIAMONDS_SEVEN)),
                        HandRanking.Type.TWO_PAIR),
                new TestCase<>(new HandRanking(Arrays.asList(Card.CLUBS_ACE, Card.HEARTS_ACE, Card.DIAMONDS_SIX, Card.DIAMONDS_TWO, Card.CLUBS_TEN, Card.SPADES_FOUR, Card.DIAMONDS_SEVEN)),
                        HandRanking.Type.ONE_PAIR),
                new TestCase<>(new HandRanking(Arrays.asList(Card.CLUBS_ACE, Card.HEARTS_SEVEN, Card.SPADES_NINE, Card.DIAMONDS_THREE, Card.CLUBS_TWO, Card.DIAMONDS_TEN, Card.CLUBS_JACK)),
                        HandRanking.Type.HIGH_CARD),
        };
        for (int i = 0; i < rankings.length ; i++) {
            TestCase<HandRanking, HandRanking.Type> iTest = rankings[i];

            assertEquals("hand " + i + " is not the correct hand type",
                    iTest.getCorrectResult(),iTest.getInput().getHandType());

            for (int j = 0 ; j < rankings.length ; j++) {
                TestCase<HandRanking, HandRanking.Type> jTest = rankings[j];
                boolean i_comparison, j_comparison = false;
                if (i == j) {
                    i_comparison = iTest.getCorrectResult().compareTo(jTest.getCorrectResult()) == 0;
                    j_comparison = jTest.getCorrectResult().compareTo(iTest.getCorrectResult()) == 0;
                } else if (i < j) {
                    i_comparison = iTest.getCorrectResult().compareTo(jTest.getCorrectResult()) > 0;
                    j_comparison = jTest.getCorrectResult().compareTo(iTest.getCorrectResult()) < 0;
                } else {
                    i_comparison = iTest.getCorrectResult().compareTo(jTest.getCorrectResult()) < 0;
                    j_comparison = jTest.getCorrectResult().compareTo(iTest.getCorrectResult()) > 0;
                }


                assertTrue("hand " + i + " is not ranked correctly against hand " + j, i_comparison);
                assertTrue("hand " + j + " is not ranked correctly against hand " + i, j_comparison);
            }
        }
    }


    @Test
    public void testWeirdCase() {
        Card[] communityCards = new Card[] {
              Card.HEARTS_TEN, Card.SPADES_THREE, Card.CLUBS_TEN, Card.SPADES_TEN, Card.DIAMONDS_FIVE
        };
        List<Card> adamCards = new ArrayList<>(Arrays.asList(communityCards));
        adamCards.addAll(Arrays.asList(Card.CLUBS_NINE, Card.CLUBS_FIVE));
        List<Card> aiCards = new ArrayList<>(Arrays.asList(communityCards));
        adamCards.addAll(Arrays.asList(Card.DIAMONDS_KING, Card.DIAMONDS_TWO));
        HandRanking adam = new HandRanking(adamCards);
        HandRanking ai = new HandRanking(aiCards);
        assertTrue(adam.compareTo(ai) > 0);
        assertTrue(ai.compareTo(adam) < 0);
    }
}
