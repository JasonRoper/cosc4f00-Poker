package com.pokerface.pokerapi.game;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pot")
public class Pot {
    public int[] pot;
    private int sum;
    private long id;
    private GameState gameState;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne(mappedBy = "pot")
    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

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
    public void add(double amount, int playerSeatID){
        pot[playerSeatID]+=amount;
        sum+=amount;
    }

    /**
     * This method returns an array containing the winnings of each player
     * @param ranking the ranking of the relative hands of each player
     * @return an array determining the amount each player has won
     */
    public int[] resolveWinnings(int[] ranking){

        int[] totalWinnings= new int[pot.length];
        int players=pot.length;
        ArrayList<Integer> winners = new ArrayList<>();


            for (int i = 1; i <= players; i++) { // THis loop ensures each player is checked for their winnings, this is the place being checked
                if (sum==0){
                    break;
                }
                for (int x = 0; x < pot.length; x++) { // This loop ensures we find them in order
                    if (ranking[x] == i) { // Then if they were the winner in order we're looking we calculate winnings
                        winners.add(x); // add them to the list of winners
                    }
                }
                while (!winners.isEmpty()) {
                    int winnings = 0;
                    int smallestWinner = Integer.MAX_VALUE;
                    for (int p : winners) { // We must determine the smallest of the winners, this means they did not contribute enough to win a full winning share
                        if (pot[p] < smallestWinner) {
                            smallestWinner = pot[p];
                        }
                    }
                    for (int x = 0; x < pot.length; x++) { // Then we must take the smallest of winners out of the pot, and split the winnings
                        if (pot[x] < smallestWinner) {
                            winnings += pot[x];
                            sum -= pot[x];
                            pot[x] = 0;
                        } else {
                            sum -= smallestWinner;
                            pot[x] -= smallestWinner;
                            winnings += smallestWinner;
                        }
                    }
                    winnings = winnings / winners.size(); // This is how much each winner deserves
                    ArrayList<Integer> newWinners = new ArrayList<>(); // This is to remove winners who get all their winnings
                    for (int p : winners) { // This is done to add their winnings, and remove them if their pot is zeroed out
                        totalWinnings[p] += winnings;
                        if (pot[p] > 0) {
                            newWinners.add(p);
                        }
                    }
                    winners.clear();
                    for (int p : newWinners) {
                        winners.add(p);
                    }
                }
                //calculate what they get here

        }


        return totalWinnings;
    }

    public int getBet (int playerSeatID) {return pot[playerSeatID];}

    public int getSum () {return sum;}

    public void setSum(int value) {sum=value;}



}
