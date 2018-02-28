package com.pokerface.pokerapi.game;

/**
 * This is an enum representing the cards in a deck. It should, eventually, store the suit and value for use by deck
 */
enum Suit {
SPADE, DIAMOND, CLUB, HEART
}

enum Rank {
    ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING

}


public class Card{
    private Suit suit;
    private Rank rank;
    Card(int roll){
        //0 is spade ace
        //13 is hearts ace
        if (roll<13){
            suit = Suit.SPADE;
        } else if (roll <26){
            suit = Suit.HEART;
        } else if (roll <39){
            suit = Suit.DIAMOND;
        } else {
            suit = Suit.CLUB;
        }
        roll=roll%13;

    }
}


//public enum Card {
//    SPADES_ACE,
//    SPADES_TWO,
//    SPADES_THREE,
//    SPADES_FOUR,
//    SPADES_FIV,
//    SPADES_SIX,
//    SPADES_SEVEN,
//    SPADES_EIGHT,
//    SPADES_NINE,
//    SPADES_TEN,
//    SPADES_JACK,
//    SPADES_QUEEN,
//    SPADES_KING,
////  Hearts
//HEARTS_ACE,
//    HEARTS_TWO,
//    HEARTS_THREE,
//    HEARTS_FOUR,
//    HEARTS_FIVE,
//    HEARTS_SIX,
//    HEARTS_SEVEN,
//    HEARTS_EIGHT,
//    HEARTS_NINE,
//    HEARTS_TEN,
//    HEARTS_JACK,
//    HEARTS_QUEEN,
//    HEARTS_KING,
////  Diamonds
//DIAMONDS_ACE,
//    DIAMONDS_TWO,
//    DIAMONDS_THREE,
//    DIAMONDS_FOUR,
//    DIAMONDS_FIVE,
//    DIAMONDS_SIX,
//    DIAMONDS_SEVEN,
//    DIAMONDS_EIGHT,
//    DIAMONDS_NINE,
//    DIAMONDS_TEN,
//    DIAMONDS_JACK,
//    DIAMONDS_QUEEN,
//    DIAMONDS_KING,
//// Clubs
//CLUBS_ACE,
//    CLUBS_TWO,
//    CLUBS_THREE,
//    CLUBS_FIVE,
//    CLUBS_SIX,
//    CLUBS_SEVEN,
//    CLUBS_EIGHT,
//    CLUBS_NINE,
//    CLUBS_TEN,
//    CLUBS_JACK,
//    CLUBS_QUEEN,
//    CLUBS_KING,
//}
