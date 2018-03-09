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
        TreeMap<Card.Suit, ArrayList<Card>> flush = new TreeMap<>();

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
            } else {
                setCount = 0;
            }

            if (lastCard == null || lastCard.next() == card) {
                workingStraight.add(card);
            } else if (workingStraight.size() >= 5){
                bestStraight = workingStraight;
            }

            flush.computeIfAbsent(card.suit(), k -> new ArrayList<>()).add(card);
        }

        boolean isStraightFlush = isFlush(bestStraight);


//        if (bestStraight != null
//                && bestStraight.get(bestStraight.size() - 1).rank() == Card.Rank.KING
//                && isStraightFlush){
//            rank =Type.ROYAL_FLUSH;
//        } else if (bestStraight != null && isStraightFlush) {
//            rank = Type.STRAIGHT_FLUSH;
//        } else if (sets.size() != null && last.getFirst() == 4) {
//            rank =Type.FOUR_OF_A_KIND;
//        } else if (last != null && secondLast != null &&
//                last.getFirst() == 3 && secondLast.getFirst() == 2) {
//            rank = Type.FULL_HOUSE;
//        } else if (flush) {
//            rank = Type.FLUSH;
//        } else if (straight) {
//            rank = Type.STRAIGHT;
//        } else if (last != null && last.getFirst() == 3) {
//            rank = Type.THREE_OF_A_KIND;
//        } else if (last != null && last.getFirst() == 2 &&
//                secondLast != null && secondLast.getFirst() == 2) {
//            rank = Type.TWO_PAIR;
//        } else if (last != null && last.getFirst() == 2) {
//            rank = Type.ONE_PAIR;
//        } else {
//            rank = Type.HIGH_CARD;
//        }
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
