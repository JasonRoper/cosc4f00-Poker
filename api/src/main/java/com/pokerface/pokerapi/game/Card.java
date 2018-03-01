package com.pokerface.pokerapi.game;

/**
 * This is an enum representing the cards in a deck. It should, eventually, store the suit and value for use by deck
 */
public enum Card {
    SPADES_ACE,
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
//  Hearts
HEARTS_ACE,
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
//  Diamonds
DIAMONDS_ACE,
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
    // Clubs
    CLUBS_ACE,
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
    CLUBS_KING;

    public static Card clubs(int i) {
        return values()[CLUBS_ACE.ordinal() + i];
    }

    public static Card hearts(int i) {
        return values()[HEARTS_ACE.ordinal() + i];
    }

    public static Card diamonds(int i) {
        return values()[DIAMONDS_ACE.ordinal() + i];
    }

    public static Card spades(int i) {
        return values()[SPADES_ACE.ordinal() + i];
    }
}
