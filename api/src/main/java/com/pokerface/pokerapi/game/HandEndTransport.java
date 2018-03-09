package com.pokerface.pokerapi.game;

import java.util.List;

public class HandEndTransport {
    int[] winnings;
    PlayerTransport[] players;

    public HandEndTransport(){

    }

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
