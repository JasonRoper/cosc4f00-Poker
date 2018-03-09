package com.pokerface.pokerapi.game;

import org.springframework.data.util.Pair;

import java.util.*;

public class HandRanking implements Comparable<HandRanking> {

    private List<Card> cards;
    private Type rank;
    private List<Card> tieBreakers;

    public HandRanking(List<Card> cards) {
        cards.sort(Card.rankCompare());
        this.cards = cards;
        
        ArrayList<Card> bestStraight = null;
        TreeMap<Card.Suit, ArrayList<Card>> suits = new TreeMap<>();

        ArrayList<Pair<Integer, Card.Rank>> sets = new ArrayList<>();

        ArrayList<Card> workingStraight = new ArrayList<>();
        int setCount = 0;

        Card lastCard = null;
        for (int i = 0 ; i < cards.size() ; i++) {
            Card card = cards.get(i);

            if (lastCard == null || lastCard.rank() == card.rank()) {
                setCount++;
            } else if (setCount > 1) {
                sets.add(Pair.of(setCount, lastCard.rank()));
                setCount = 1;
            } else {
                setCount = 1;
            }

            if (lastCard == null || lastCard.next().rank() == card.rank()) {
                workingStraight.add(card);
            } else if (workingStraight.size() >= 5){
                bestStraight = workingStraight;
            } else if (lastCard.rank() != card.rank()){
                workingStraight.clear();
                workingStraight.add(card);
            }

            suits.computeIfAbsent(card.suit(), k -> new ArrayList<>());
            suits.get(card.suit()).add(card);

            lastCard = card;
        }

        if (setCount > 1) {
            sets.add(Pair.of(setCount, lastCard.rank()));
        }

        ArrayList<Card> flush = null;
        for (ArrayList<Card> flushCandidate : suits.values()) {
            if (flushCandidate.size() >= 5) {
                flush = flushCandidate;
                break;
            }
        }

        boolean isStraightFlush = false;
        if (flush != null && bestStraight != null)
            isStraightFlush = isFlush(bestStraight);

        sets.sort(pairComparator());


        if (bestStraight != null
                && bestStraight.get(bestStraight.size() - 1).rank() == Card.Rank.KING
                && isStraightFlush){
            rank =Type.ROYAL_FLUSH;
        } else if (bestStraight != null && isStraightFlush) {
            rank = Type.STRAIGHT_FLUSH;
         } else if (sets.size() != 0 && sets.get(sets.size() - 1).getFirst() == 4) {
            rank = Type.FOUR_OF_A_KIND;
        } else if (sets.size() >= 2 && sets.get(sets.size() - 1).getFirst() == 3 &&
                sets.get(sets.size() - 2).getFirst() == 2) {
            rank = Type.FULL_HOUSE;
        } else if (flush != null) {
            rank = Type.FLUSH;
        } else if (bestStraight != null) {
            rank = Type.STRAIGHT;
        } else if (sets.size() != 0 && sets.get(sets.size() - 1).getFirst() == 3) {
            rank = Type.THREE_OF_A_KIND;
        } else if (sets.size() >= 2 && sets.get(sets.size() - 1).getFirst() == 2 &&
                sets.get(sets.size() - 2).getFirst() == 2) {
            rank = Type.TWO_PAIR;
        } else if (sets.size() != 0 && sets.get(sets.size() - 1).getFirst() == 2) {
            rank = Type.ONE_PAIR;
        } else {
            rank = Type.HIGH_CARD;
        }
    }

    @Override
    public int compareTo(HandRanking ranking) {
        if (ranking.rank.ordinal() != rank.ordinal()) {
            return rank.compareTo(ranking.rank);
        }

        Comparator<Card> compare = Card.rankCompare();
        for (int i = 0 ; i < cards.size(); i++) {
            int res = compare.compare(tieBreakers.get(i), ranking.tieBreakers.get(i));
            if (res != 0) {
                return res;
            }
        }

        throw new RuntimeException("A valid compare was not found");
    }

    public boolean isFlush(List<Card> cards){
        Card.Suit suit = null;
        for (Card card : cards) {
            if (suit == null) {
                suit = card.suit();
            } if (card.suit() != suit) {
                return false;
            }
        }
        return true;
    }

    public Type getRank() {
        return rank;
    }

    public static Comparator<Pair<Integer,Card.Rank>> pairComparator() {
        return (l, r) -> {
            int comp = Integer.compare(l.getFirst(), r.getFirst());
            if (comp == 0) {
                return l.getSecond().compareTo(r.getSecond());
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
