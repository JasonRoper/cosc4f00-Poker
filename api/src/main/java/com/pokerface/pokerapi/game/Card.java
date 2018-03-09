package com.pokerface.pokerapi.game;

import java.util.Comparator;

/**
 * This is an enum representing the cards in a deck. It has functionality
 * to break that Card down into Suit and Rank values
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

    // Clubs,
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

    /**
     * This function operates on Card's to provide their suit value, it uses their ordinal position to determine suit.
     * @return a Suit, which is Spades Clubs Diamonds or Hearts
     */
    public Suit suit() {
        Card ace = Card.values()[(this.ordinal() / 13) * 13];
        switch(ace) {
            case SPADES_ACE:
                return Suit.SPADES;
            case CLUBS_ACE:
                return Suit.CLUBS;
            case DIAMONDS_ACE:
                return Suit.DIAMONDS;
            case HEARTS_ACE:
                return Suit.HEARTS;
            default:
                return null;
        }
    }

    /**
     * Checks whether or not a Card matches the given suit
     * @param suit the suit to check
     * @return the boolean of if the suits match
     */
    public boolean isSuit(Suit suit) {
        return this.suit() == suit;
    }

    /**
     * Rank returns the value of the rank of the card, which is their mod 13 value. 1 is ace, 13 is king
     * @return Rank representing Ace-King
     */
    public Rank rank() {
        return Rank.values()[this.ordinal() % 13];
    }

    /**
     * Checks if a card is a face card, used for hand evaluation
     * @param rank the Rank to check if it is Face
     * @return true if face, false if not a face card
     */
    public boolean isFace(Rank rank){
        return this.rank() == rank;
    }

    /**
     * Checks the next card up, for hand evaluation
     * @return returns the next card from ordinal position
     */
    public Card next() {
        int next = (this.ordinal() + 1) % 13 + (this.ordinal() / 13) * 13;

        Card nextCard = Card.values()[next];
        return (nextCard.suit() == this.suit()) ? nextCard : null;
    }

    /**
     * Compares the rank, sorting them by rank
     * @return Comparator of the handed cards
     */
    public static Comparator<Card> rankCompare() {
        return new Comparator<Card>() {
            @Override
            public int compare(Card l, Card r) {
                return l.rank().compareTo(r.rank());
            }
        };
    }

    /**
     * Returns a suit comparison of the cards
     * @return a comparator object of the two cards suit
     */
    public static Comparator<Card> suitCompare() {
        return new Comparator<Card>() {
            @Override
            public int compare(Card l, Card r) {
                return l.suit().compareTo(r.suit());
            }
        };
    }

    /**
     * The suits of playing cards, Spades, CLubs, Diamonds and Hearts
     */
    public enum Suit {
        SPADES, CLUBS, DIAMONDS, HEARTS
    }

    /**
     * The Rank of playing cards, from Ace to King
     */
    public enum Rank {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING
    }
}
