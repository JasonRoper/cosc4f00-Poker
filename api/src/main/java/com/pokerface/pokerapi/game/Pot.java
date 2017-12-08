package com.pokerface.pokerapi.game;

public class Pot {
    int[] pot;
    int sum=0;
    public Pot(int players){
        pot=new int[players];
    }

    public int add(int amount, int playerSeatID){
        sum+=amount;
        pot[playerSeatID]+=amount;
        return sum;
    }

    public int[] winnings(int[] winningPlayerSeatIDs,boolean[] splitPot){
        int winnings[]=new int[winningPlayerSeatIDs.length];
        int pos=0;//Position going through the winners
        while (sum!=0) {
            if (!splitPot[pos]) {
                for (int i = 0; i < pot.length; i++) {//Going through each of the availabl pots to that winner;
                    int amount = pot[winningPlayerSeatIDs[pos]];//So we don't need to keep referencing how much that person put into the pot
                    pot[i]-=amount; // Try to take that amount out of the pot
                    if (pot[i] < 0) { // If it is negative, return that difference
                        amount += pot[i];
                        pot[i] = 0;
                    }


                    sum -= amount; // Add what's left.
                    winnings[winningPlayerSeatIDs[pos]] += amount; // Sum the amount you were able to take
                }

            } else {
                //HANDLE SPLIT POTS IMPORTANCE:major
            }
            pos++; // advance to the next person in the winners list
        }
    return winnings;
    }

}
