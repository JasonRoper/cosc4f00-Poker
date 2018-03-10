package com.pokerface.pokerapi.game;

import java.util.Comparator;

/**
 * This is an enum representing the cards in a deck. It should, eventually, store the suit and value for use by deck
 */
public enum Card {
    SPADES_TWO,
    SPADES_THREE,
    SPADES_FOUR,
    SPADES_FIVE,
    SPADES_SIX,
    SPADES_SEVEN,
    SPADES_EIGHT,
    SPADES_NINE,
    SPADES_TEN,
    SPADES_JACK,
    SPADES_QUEEN,
    SPADES_KING,
    SPADES_ACE,
//  Hearts
    HEARTS_TWO,
    HEARTS_THREE,
    HEARTS_FOUR,
    HEARTS_FIVE,
    HEARTS_SIX,
    HEARTS_SEVEN,
    HEARTS_EIGHT,
    HEARTS_NINE,
    HEARTS_TEN,
    HEARTS_JACK,
    HEARTS_QUEEN,
    HEARTS_KING,
    HEARTS_ACE,
//  Diamonds
    DIAMONDS_TWO,
    DIAMONDS_THREE,
    DIAMONDS_FOUR,
    DIAMONDS_FIVE,
    DIAMONDS_SIX,
    DIAMONDS_SEVEN,
    DIAMONDS_EIGHT,
    DIAMONDS_NINE,
    DIAMONDS_TEN,
    DIAMONDS_JACK,
    DIAMONDS_QUEEN,
    DIAMONDS_KING,
    DIAMONDS_ACE,
    // Clubs,
    CLUBS_TWO,
    CLUBS_THREE,
    CLUBS_FOUR,
    CLUBS_FIVE,
    CLUBS_SIX,
    CLUBS_SEVEN,
    CLUBS_EIGHT,
    CLUBS_NINE,
    CLUBS_TEN,
    CLUBS_JACK,
    CLUBS_QUEEN,
    CLUBS_KING,
    CLUBS_ACE;

    public Suit suit() {
        Card ace = Card.values()[(this.ordinal() / 13) * 13];
        switch(ace) {
            case SPADES_TWO:
                return Suit.SPADES;
            case CLUBS_TWO:
                return Suit.CLUBS;
            case DIAMONDS_TWO:
                return Suit.DIAMONDS;
            case HEARTS_TWO:
                return Suit.HEARTS;
            default:
                return null;
        }
    }

    public boolean isSuit(Suit suit) {
        return this.suit() == suit;
    }

    public Rank rank() {
        return Rank.values()[this.ordinal() % 13];
    }

    public boolean isFace(Rank rank){
        return this.rank() == rank;
    }

    public Card next() {
        int next = (this.ordinal() + 1) % 13 + (this.ordinal() / 13) * 13;

        return this.values()[next];
    }

    public static Comparator<Card> rankCompare() {
        return Comparator.comparingInt(l -> l.rank().ordinal());
    }

    public static Comparator<Card> suitCompare() {
        return Comparator.comparingInt(l -> l.suit().ordinal());
    }

    public enum Suit {
        SPADES, CLUBS, DIAMONDS, HEARTS
    }

    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
    }
}
