package com.pokerface.pokerapi.game;

import java.util.List;

/**
 * HandEndTransport is the object the sends whena hand ends and people have won money
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

    private class PlayerTransport{
        Card cardOne;
        Card cardTwo;

        public PlayerTransport(){

        }

        public PlayerTransport(Card cardOne, Card cardTwo){
            this.cardOne=cardOne;
            this.cardTwo=cardTwo;
        }
    }

}
