package com.pokerface.pokerapi.game;

import org.springframework.data.util.Pair;

import java.util.*;

public class HandRanking implements Comparable<HandRanking> {

    /**
     * cards is the sorted list of the cards this HandRanking is for.
     */
    private List<Card> cards;

    /**
     * The rank is the type of hand that this hand is scored as.
     * (ie flush, straight, pair, etc.)
     */
    private Type rank;

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
                flush.get(flush.size() - 1).rank() == Card.Rank.KING &&
                flush.get(0).rank() == Card.Rank.ACE) {
            rank = Type.ROYAL_FLUSH;
        }

        // STRAIGHT FLUSH: whether a hand is a straight flush is determined above
        // tie breaker: high card of the cards used in the straight flush
        else if (straightFlush != null) {
            rank = Type.STRAIGHT_FLUSH;
            tieBreakers.add(straightFlush.get(straightFlush.size() - 1));
        }

        // FOUR OF A KIND: a hand is four of a kind if there exists a set with a size of 4
        // tie breaker: four of a kind card, then the next highest card.
        else if (sets.size() != 0 && sets.get(sets.size() - 1).getFirst() == 4) {
            rank = Type.FOUR_OF_A_KIND;
            tieBreakers.add(sets.get(sets.size() - 1).getSecond());
            tieBreakers.add(highCardNot(cards, sets.get(sets.size() - 1).getSecond()));
        }

        // FULL HOUSE: if sets has 2 or more sets, and they respectively contain 3 and 2
        // (or more) cards.
        // tie breaker: the high card of the 3 of a kind, then the high card of pair
        else if (sets.size() >= 2 && sets.get(sets.size() - 1).getFirst() >= 3 &&
                sets.get(sets.size() - 2).getFirst() >= 2) {
            rank = Type.FULL_HOUSE;
            tieBreakers.add(sets.get(sets.size() - 1).getSecond());
            tieBreakers.add(sets.get(sets.size() - 2).getSecond());
        }

        // FLUSH:
        // tie breaker: high card within the flush
        else if (flush != null) {
            rank = Type.FLUSH;
            Collections.reverse(flush);
            tieBreakers.addAll(flush.subList(0, 5));
        }

        // STRAIGHT:
        // tie breaker: high card in straight
        else if (straight != null) {
            rank = Type.STRAIGHT;
            tieBreakers.add(straight.get(straight.size() - 1));
        }

        // THREE OF A KIND:
        // tie breaker: three of a kind card, then the two high cards remaining
        else if (sets.size() != 0 && sets.get(sets.size() - 1).getFirst() == 3) {
            rank = Type.THREE_OF_A_KIND;
            tieBreakers.add(sets.get(sets.size() - 1).getSecond());

            List<Card> highCards = filterOut(cards, tieBreakers.get(0));
            Collections.reverse(highCards);
            tieBreakers.addAll(highCards.subList(0, Math.min(2, highCards.size())));
        }

        // TWO PAIR:
        // tie breaker: high pair, second pair, high card
        else if (sets.size() >= 2 && sets.get(sets.size() - 1).getFirst() == 2 &&
                sets.get(sets.size() - 2).getFirst() == 2) {
            rank = Type.TWO_PAIR;
            Card first = sets.get(sets.size() - 1).getSecond();
            Card second = sets.get(sets.size() - 2).getSecond();
            if (first.rank().compareTo(second.rank()) > 0) {
                tieBreakers.add(first);
                tieBreakers.add(second);
            } else {
                tieBreakers.add(second);
                tieBreakers.add(first);
            }
            tieBreakers.add(highCardNot(cards, first, second));

        }

        // PAIR:
        // tie breaker: high pair, high card of 3
        else if (sets.size() != 0 && sets.get(sets.size() - 1).getFirst() == 2) {
            rank = Type.ONE_PAIR;
            tieBreakers.add(sets.get(sets.size() - 1).getSecond());
            List<Card> remaining = filterOut(cards, tieBreakers.get(0));
            Collections.reverse(remaining);
            tieBreakers.addAll(remaining.subList(0, Math.min(3, remaining.size())));
        }
        // HIGH CARD:
        // tie breaker: highest card for 5
        else {
            rank = Type.HIGH_CARD;
            List<Card> highCards = new ArrayList<>(cards);
            Collections.reverse(highCards);
            tieBreakers.addAll(highCards.subList(0,Math.min(5, highCards.size())));
        }
    }

    private List<Card> findStraight(List<Card> cards) {
        // straight will be set if a straight is found
        ArrayList<Card> straight = null;

        // workingStraight stores the current straight that is being built
        ArrayList<Card> workingStraight = new ArrayList<>();

        Card lastCard = null;
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);

            // figure out if the next card should be added to the workingStraight
            // if it isn't, check to see if the workingStraight should be set to
            // straight.
            if (lastCard == null || lastCard.next().rank() == card.rank()) {
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

        // if the last card was a KING, then if an ACE exists at the start of the list
        // of cards, that ace should be added to the workingStraight
        if (lastCard.rank() == Card.Rank.KING && cards.get(0).rank() == Card.Rank.ACE) {
            workingStraight.add(cards.get(0));
        }

        // if the workingStraight is a valid straight, set straight
        if (workingStraight.size() >= 5) {
            straight = workingStraight;
        }

        return straight;
    }

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

    private List<Card> findFlush(List<Card> cards) {
        // suits stores all cards with each suit
        TreeMap<Card.Suit, ArrayList<Card>> suits = new TreeMap<>();
        for (Card card : cards) {
            // don't create an array for suits unless that suit actually exists
            suits.computeIfAbsent(card.suit(), k -> new ArrayList<>());

            // add the card to its associated suit array
            suits.get(card.suit()).add(card);
        }

        // test to see if there is a valid flush in the cards. This occurs when
        // there are more than five cards in a given suit array
        ArrayList<Card> flush = null;
        for (ArrayList<Card> flushCandidate : suits.values()) {
            if (flushCandidate.size() >= 5) {
                flush = flushCandidate;
                break;
            }
        }

        return flush;
    }

    private Card highCardNot(List<Card> cards, Card ...not){
        List<Card> notList = Arrays.asList(not);
        for (int i = cards.size() - 1 ; i >= 0 ; i--){
            if (!notList.contains(cards.get(i))) {
                return cards.get(i);
            }
        }
        return null;
    }

    private List<Card> filterOut(List<Card> cards, Card ...filter) {
        ArrayList<Card> newCards = new ArrayList<>(cards);
        List<Card> filterList = Arrays.asList(filter);
        newCards.removeIf(card -> filterList.contains(card));
        return newCards;
    }

    @Override
    public int compareTo(HandRanking ranking) {
        if (ranking.rank != rank) {
            return rank.compareTo(ranking.rank);
        }

        Comparator<Card> compare = Card.rankCompare();
        for (int i = 0; i < tieBreakers.size(); i++) {
            int res = compare.compare(tieBreakers.get(i), ranking.tieBreakers.get(i));
            if (res != 0) {
                return res;
            }
        }

        return 0;
    }


    public Type getRank() {
        return rank;
    }

    public static Comparator<Pair<Integer, Card>> pairComparator() {
        return (l, r) -> {
            int comp = Integer.compare(l.getFirst(), r.getFirst());
            if (comp == 0) {
                return Card.rankCompare().compare(l.getSecond(), r.getSecond());
            }
            return comp;
        };
    }

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
