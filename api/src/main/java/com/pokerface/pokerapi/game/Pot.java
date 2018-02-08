package com.pokerface.pokerapi.game;

import java.util.ArrayList;
import java.util.List;

public class Pot {
    private int[] pot;
    private int sum;
    public Pot(int players){
        pot=new int[players];
        sum=0;
    }


    /**
     * Super not done, an idea at best. This is the amount of money each person has put into the pot.
     *
     * @param amount
     * @param playerSeatID
     * @return
     */
    public void add(int amount, int playerSeatID){
        pot[playerSeatID]+=amount;
        sum+=sum;
    }

    /**
     * This method returns an array containing the winnings of each player
     * @param ranking the ranking of the relative hands of each player
     * @return an array determining the amount each player has won
     */
    public int[] resolveWinnings(int[] ranking){
        int winnings=0;
        int[] totalWinnings= new int[pot.length];
        int players=pot.length;
        ArrayList winners = new ArrayList<>();

        for (int i=1;i<=players;i++){ // THis loop ensures each player is checked for their winnings
            for (int x=0;x<pot.length;x++){ // This loop ensures we find them in order
                if (ranking[x]==i){ // Then if they were the winner in order we're looking we calculate winnings
                    winners.add(x); // add them to the list of winners
                }
            }
            //calculate what they get here
        }



        return totalWinnings;
    }

    public int getSum () {return sum;}

    public void resetSum() {sum=0;}



}
