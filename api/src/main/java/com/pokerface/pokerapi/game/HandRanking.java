package com.pokerface.pokerapi.game;

import org.springframework.data.util.Pair;

import java.util.*;

/**
 * HandRanking is used to evaluate hands by the GameService at a hands end to see who won, this ranking is then used by
 * the pot to evaluate who gets what winnings.
 *
 * HandRanking is also used to evaluate AI position for move decision, it will consider what hand it has and then modify
 * its actions based on that.
 */
public class HandRanking implements Comparable<HandRanking> {

    /**
     * cards is the sorted list of the cards this HandRanking is for.
     */
    private List<Card> cards;

    /**
     * The rank is the type of hand that this hand is scored as.
     * (ie flush, straight, pair, etc.)
     */
    private Type handType;

    /**
     * the tieBreakers are a list of cards occurring in the order that
     * they should be used to determine who won in the event of a tie.
     */
    private List<Card> tieBreakers;

    /**
     * Create a HandRanking from a list of cards.
     *
     * @param cards One or more cards in any order.
     */
    public HandRanking(List<Card> cards) {
        cards.sort(Card.rankCompare());
        this.cards = cards;
        this.tieBreakers = new ArrayList<>();
        rankCards();
    }

    /**
     * rankCards finds out what type of hand the cards make, as well
     * as determining the tiebreaker order.
     */
    private void rankCards() {

        // straight stores a valid straight. null if straight doesn't exist
        List<Card> straight = findStraight(cards);

        // sets tracks which cards ranks have been repeated (for pairs, 3
        // of a kind, etc.)
        List<Pair<Integer, Card>> sets = findSets(cards);

        // if there is a valid flush in the cards, store it in flush, otherwise null.
        List<Card> flush = findFlush(cards);

        List<Card> straightFlush = null;

        // if a flush exists, test to see if there is a straight contained within it.
        if (flush != null) {
            straightFlush = findStraight(flush);
        }

        // ROYAL FLUSH: a hand is a royal flush if it is a straight flush, the high
        // card is a KING, and the first card is an ACE
        // tie breaker: NONE
        if (straightFlush != null &&
                straightFlush.get(straightFlush.size() - 1).rank() == Card.Rank.ACE) {
            handType = Type.ROYAL_FLUSH;
        }

        // STRAIGHT FLUSH: whether a hand is a straight flush is determined above
        // tie breaker: high card of the cards used in the straight flush
        else if (straightFlush != null) {
            handType = Type.STRAIGHT_FLUSH;
            tieBreakers.add(straightFlush.get(straightFlush.size() - 1));
        }

        // FOUR OF A KIND: a hand is four of a kind if there exists a set with a size of 4
        // tie breaker: four of a kind card, then the next highest card.
        else if (sets.size() != 0 && sets.get(sets.size() - 1).getFirst() == 4) {
            handType = Type.FOUR_OF_A_KIND;
            tieBreakers.add(sets.get(sets.size() - 1).getSecond());
            tieBreakers.add(highCardNotRank(cards, sets.get(sets.size() - 1).getSecond().rank()));
        }

        // FULL HOUSE: if sets has 2 or more sets, and they respectively contain 3 and 2
        // (or more) cards.
        // tie breaker: the high card of the 3 of a kind, then the high card of pair
        else if (sets.size() >= 2 && sets.get(sets.size() - 1).getFirst() >= 3 &&
                sets.get(sets.size() - 2).getFirst() >= 2) {
            handType = Type.FULL_HOUSE;
            tieBreakers.add(sets.get(sets.size() - 1).getSecond());
            tieBreakers.add(sets.get(sets.size() - 2).getSecond());
        }

        // FLUSH:
        // tie breaker: high card within the flush
        else if (flush != null) {
            handType = Type.FLUSH;
            Collections.reverse(flush);
            tieBreakers.addAll(flush.subList(0, 5));
        }

        // STRAIGHT:
        // tie breaker: high card in straight
        else if (straight != null) {
            handType = Type.STRAIGHT;
            tieBreakers.add(straight.get(straight.size() - 1));
        }

        // THREE OF A KIND:
        // tie breaker: three of a kind card, then the two high cards remaining
        else if (sets.size() != 0 && sets.get(sets.size() - 1).getFirst() == 3) {
            handType = Type.THREE_OF_A_KIND;
            tieBreakers.add(sets.get(sets.size() - 1).getSecond());

            List<Card> highCards = filterOutRank(cards, tieBreakers.get(0).rank());
            Collections.reverse(highCards);
            tieBreakers.addAll(highCards.subList(0, Math.min(2, highCards.size())));
        }

        // TWO PAIR:
        // tie breaker: high pair, second pair, high card
        else if (sets.size() >= 2 && sets.get(sets.size() - 1).getFirst() == 2 &&
                sets.get(sets.size() - 2).getFirst() == 2) {
            handType = Type.TWO_PAIR;
            Card first = sets.get(sets.size() - 1).getSecond();
            Card second = sets.get(sets.size() - 2).getSecond();
            if (first.rank().compareTo(second.rank()) > 0) {
                tieBreakers.add(first);
                tieBreakers.add(second);
            } else {
                tieBreakers.add(second);
                tieBreakers.add(first);
            }
            tieBreakers.add(highCardNotRank(cards, first.rank(), second.rank()));

        }

        // PAIR:
        // tie breaker: high pair, high card of 3
        else if (sets.size() != 0 && sets.get(sets.size() - 1).getFirst() == 2) {
            handType = Type.ONE_PAIR;
            tieBreakers.add(sets.get(sets.size() - 1).getSecond());
            List<Card> remaining = filterOutRank(cards, tieBreakers.get(0).rank());
            Collections.reverse(remaining);
            tieBreakers.addAll(remaining.subList(0, Math.min(3, remaining.size())));
        }
        // HIGH CARD:
        // tie breaker: highest card for 5
        else {
            handType = Type.HIGH_CARD;
            List<Card> highCards = new ArrayList<>(cards);
            Collections.reverse(highCards);
            tieBreakers.addAll(highCards.subList(0,Math.min(5, highCards.size())));
        }
    }

    /**
     * findStraight takes in a sorted list of cards, and returns a straight if it finds one.
     *
     * @param cards a sorted list of cards
     * @return a straight in sorted order, or null
     */
    private List<Card> findStraight(List<Card> cards) {
        // straight will be set if a straight is found
        ArrayList<Card> straight = null;

        // workingStraight stores the current straight that is being built
        ArrayList<Card> workingStraight = new ArrayList<>();

        // Initialize lastCard to the last element of the cards list in case
        // the high card of the hand is an ace. this will allow aces to be a
        // part of an A, 2, 3, 4, 5 straight as well as a 10, J, Q, K, A straight
        Card lastCard = cards.get(cards.size() - 1);
        workingStraight.add(lastCard);

        for (Card card : cards) {

            // figure out if the next card should be added to the workingStraight
            // if it isn't, check to see if the workingStraight is a valid straight.
            if (lastCard.next().rank() == card.rank()) {
                workingStraight.add(card);
            } else if (lastCard.rank() != card.rank()) {
                if (workingStraight.size() >= 5) {
                    straight = workingStraight;
                    workingStraight = new ArrayList<>();
                } else {
                    workingStraight.clear();
                }

                workingStraight.add(card);
            }

            lastCard = card;
        }

        // if the workingStraight is a valid straight, set straight
        if (workingStraight.size() >= 5) {
            straight = workingStraight;
        }

        return straight;
    }

    /**
     * findSets finds all repeated card ranks in a list, and returns a sorted list
     * of which the ranks, along with the number of times it appears.
     * @param cards a sorted list of cards
     * @return a list of pairs containing a count of how many times the given rank appears
     *         and a card that is a member of the set. if no pairs exist, the list will be empty
     */
    private List<Pair<Integer, Card>> findSets(List<Card> cards) {
        // of a kind, etc.)
        List<Pair<Integer, Card>> sets = new ArrayList<>();

        // setCount counts how many cards with a given rank have appeared
        // in a row.
        int setCount = 0;

        Card lastCard = null;
        for (Card card : cards) {

            // figure out if the current card is the same rank as the last one,
            // and add it to setCount. If it isn't, add the set to lists if it,
            // is a pair or greater
            if (lastCard == null || lastCard.rank() == card.rank()) {
                setCount++;
            } else if (setCount > 1) {
                sets.add(Pair.of(setCount, lastCard));
                setCount = 1;
            } else {
                setCount = 1;
            }

            lastCard = card;
        }

        // if a set wasn't saved to sets when we left the loop, save it now
        if (setCount > 1) {
            sets.add(Pair.of(setCount, lastCard));
        }

        sets.sort(pairComparator());

        return sets;
    }

    /**
     * findFlush tests to see if the given cards contain a flush.
     *
     * @param cards the list of cards to test
     * @return if a flush exists, return all cards that are a member of that suit.
     *         if a flush doesn't exist, returns null.
     */
    private List<Card> findFlush(List<Card> cards) {
        // suits stores all cards with each suit
        EnumMap<Card.Suit, ArrayList<Card>> suits = new EnumMap<>(Card.Suit.class);
        for (Card card : cards) {
            // don't create an array for suits unless that suit actually exists
            suits.computeIfAbsent(card.suit(), k -> new ArrayList<>());

            // add the card to its associated suit array
            suits.get(card.suit()).add(card);
        }

        // test to see if there is a valid flush in the cards. This occurs when
        // there are more than five cards in a given suit array
        for (ArrayList<Card> flush : suits.values()) {
            if (flush.size() >= 5) {
                return flush;
            }
        }
        
        return null;
    }

    /**
     * highCardNotRank takes in a sorted list of cards, and returns the highest card
     * within that list that does not have the same rank as another card in the not list.
     * 
     * @param cards the list of cards to use
     * @param not the list of ranks to ignore when looking through cards
     * @return the highest card in cards that does not have a rank in not
     */
    private Card highCardNotRank(List<Card> cards, Card.Rank ...not){
        List<Card.Rank> notList = Arrays.asList(not);
        for (int i = cards.size() - 1 ; i >= 0 ; i--){
            if (!notList.contains(cards.get(i).rank())) {
                return cards.get(i);
            }
        }
        return null;
    }

    /**
     * filterOutRank takes in a list of cards, and returns a copy of the list without
     * the ranks in filter
     * 
     * @param cards the list of cards to filter
     * @param filter the list of ranks to filter out of cards
     * @return the cards list without any cards that have a rank in filter
     */
    private List<Card> filterOutRank(List<Card> cards, Card.Rank ...filter) {
        ArrayList<Card> newCards = new ArrayList<>(cards);
        List<Card.Rank> filterList = Arrays.asList(filter);
        newCards.removeIf(card -> filterList.contains(card.rank()));
        return newCards;
    }

    /**
     * compareTo checks to see whether this hand is ranked above, below, or is tied with another hand. 
     * 1 if this hand wins against otherHand, 0 if this hand is tied with otherHand, and -1 if this hand
     * looses to otherHand.
     * 
     * @param otherHand the HandRanking to compare this hand to
     * @return whether or not this hand wins, ties, or looses to otherHand
     */
    @Override
    public int compareTo(HandRanking otherHand) {
        if (otherHand.handType != handType) {
            return handType.compareTo(otherHand.handType);
        }

        Comparator<Card> compare = Card.rankCompare();
        for (int i = 0; i < tieBreakers.size(); i++) {
            int res = compare.compare(tieBreakers.get(i), otherHand.tieBreakers.get(i));
            if (res != 0) {
                return res;
            }
        }

        return 0;
    }


    /**
     * get the hand type of this hand (pair, two pair, flush, etc.)
     * @return the type of hand
     */
    public Type getHandType() {
        return handType;
    }

    /**
     * return a comparator that will be able to sort sets. of cards.
     * this comparator will rank sets with more cards over sets with
     * less (ie, three of a kind over pair) and will sort sets of equal
     * size by rank (ie. a pair of aces over a pair of jacks)
     *
     * @return the set comparator
     */
    private static Comparator<Pair<Integer, Card>> pairComparator() {
        return (l, r) -> {
            int comp = Integer.compare(l.getFirst(), r.getFirst());
            if (comp == 0) {
                return Card.rankCompare().compare(l.getSecond(), r.getSecond());
            }
            return comp;
        };
    }

    /**
     * Type is all of the types of hands in a game of poker
     */
    enum Type {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        STRAIGHT,
        FLUSH,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        STRAIGHT_FLUSH,
        ROYAL_FLUSH
    }
}
