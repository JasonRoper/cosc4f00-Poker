package com.pokerface.pokerapi.game;

import java.util.List;

/**
 * HandEndTransport is the object the sends when a hand ends and people have won money
 */
public class HandEndTransport {
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
            this.players[i]=new PlayerTransport(players.get(i).getPlayerID(),players.get(i).getCashOnHand(),players.get(i).getLastGameAction(),players.get(i).isAI(),players.get(i).isDealer(),players.get(i).getHasFolded(),winnings[i],players.get(i).getCardOne(),players.get(i).getCardTwo(),players.get(i).getBet(),players.get(i).getName());
        }

    }



}
