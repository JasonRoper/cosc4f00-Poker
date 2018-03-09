package com.pokerface.pokerapi.game;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Pot is a representation of the money being held for a hand of Poker. It holds money, and also knows who has put in what amount.
 *
 * This exists within a GameState. It has methods to return who has bet what, as well as, if given a ranking array, who will win what amounts at the end of a Hand.
 */
@Entity
@Table(name = "pot")
public class Pot {
    public int[] pot;
    private int sum;
    private long id;
    private GameState gameState;

    /**
     * Return the ID of the pot
     * @return long of the ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    /**
     * sets the ID of the pot
     * @param id the long to be set as the ID of the pot
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * the GameState of the pot
     * @return the GameState of the pot
     */
    @OneToOne(mappedBy = "pot")
    public GameState getGameState() {
        return gameState;
    }

    /**
     * setGameState sets the gameState of the pot
     * @param gameState the GameState to be set
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Creates a pot of size players
     * @param players int of players
     */
    public Pot(int players){
        pot=new int[players];
        sum=0;
    }


    /**
     * Adds the amount to the pot the PlayerID bet
     *
     * @param amount bet, as int
     * @param playerSeatID the player betting
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
        }
        return totalWinnings;
    }

    /**
     * Returns how much that player has bet
     * @param playerSeatID the plaer to check
     * @return
     */
    public int getBet (int playerSeatID) {return pot[playerSeatID];}

    /**
     * Returns the sum of the pot
     * @return the int of the sum of the pot
     */
    public int getSum () {return sum;}

    /**
     * Sets the sum of the pot, used to reset.
     * @param value to set the pot
     */
    public void setSum(int value) {sum=value;}



}
