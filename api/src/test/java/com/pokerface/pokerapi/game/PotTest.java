package com.pokerface.pokerapi.game;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PotTest {
    Pot pot;
    int[] moneyInPot;
    int[] ranking;
    int[] expectedWinnings;

    void insertPot(){
        for (int i=0;i<pot.getPlayerCount();i++){
            pot.add(moneyInPot[i],i);
        }
    }

    boolean matches(int[] winnings){
        for (int i=0;i<winnings.length;i++){
            if (winnings[i]!=expectedWinnings[i]){
                System.out.println(winnings[i]+" = " + expectedWinnings[i]);
                return false;
            }
        }
        return true;
    }


    @Test
    public void testSingleWinnerEvenPot(){
        pot=new Pot(4);
        moneyInPot= new int[]{100,100,100,100};
        ranking = new int[]{1,2,3,4};
        expectedWinnings = new int[]{400,0,0,0};
        insertPot();
        assertTrue(matches(pot.resolveWinnings(ranking)));
    }

    @Test
    public void testSingleWinnerUnevenPot(){

    }

    @Test
    public void testMultipleWinnersEvenPot(){

    }

    @Test
    public void testMultipleWinnersUnevenPot(){

    }
}
