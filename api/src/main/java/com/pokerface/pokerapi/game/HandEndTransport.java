package com.pokerface.pokerapi.game;

import java.util.List;

/**
 * HandEndTransport is the object the sends when a hand ends and people have won money
 */
public class HandEndTransport {
    int[] winnings;
    PlayerTransport[] players;

    public HandEndTransport(){

    }

    /**
     * To Create it it needs an int array of the winnings and a list of player objects containing their cards
     * @param winnings the int value of money won and by whom
     * @param players the players cards, revealing who had what
     */
    public HandEndTransport(int[] winnings, List<Player> players){
        this.players = new PlayerTransport[players.size()];
        for (int i=0;i<players.size();i++){
            this.players[i]=new PlayerTransport(players.get(i).getCardOne(),players.get(i).getCardTwo());
        }

    }

    /**
     * A {@link HandEndTransport.PlayerTransport} represents the cards of a player. It is used to deliver
     * the cards that a player has at the end of a hand.
     */
    private class PlayerTransport{
        Card cardOne;
        Card cardTwo;

        public PlayerTransport(){

        }

        /**
         * Create a {@link HandEndTransport.PlayerTransport} with the given cards
         *
         * @param cardOne the players first card
         * @param cardTwo the players second card
         */
        public PlayerTransport(Card cardOne, Card cardTwo){
            this.cardOne=cardOne;
            this.cardTwo=cardTwo;
        }
    }

}
