package com.pokerface.pokerapi.game;

import org.junit.Test;

import java.io.PrintWriter;

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

    boolean matches(int[] winnings, int[] expectedWinnings){
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
        assertTrue(matches(pot.resolveWinnings(ranking),expectedWinnings));

        ranking = new int[]{4,2,3,1};
        expectedWinnings = new int[]{0,0,0,400};
        insertPot();
        assertTrue(matches(pot.resolveWinnings(ranking),expectedWinnings));

        ranking = new int[]{3,1,2,4};
        expectedWinnings = new int[]{0,400,0,0};
        insertPot();
        assertTrue(matches(pot.resolveWinnings(ranking),expectedWinnings));

        moneyInPot= new int[]{200,200,200,200};
        ranking = new int[]{1,2,3,3};
        expectedWinnings = new int[]{800,0,0,0};
        insertPot();
        assertTrue(matches(pot.resolveWinnings(ranking),expectedWinnings));
    }

    @Test
    public void testSingleWinnerUnevenPot(){
        pot=new Pot(4);
        moneyInPot= new int[]{150,200,50,100};
        ranking = new int[]{1,2,3,4};
        expectedWinnings = new int[]{450,50,0,0};
        insertPot();
        assertTrue(matches(pot.resolveWinnings(ranking),expectedWinnings));

        moneyInPot= new int[]{150,200,50,100};
        ranking = new int[]{2,1,4,3};
        expectedWinnings = new int[]{0,500,0,0};
        insertPot();
        assertTrue(matches(pot.resolveWinnings(ranking),expectedWinnings));

        moneyInPot= new int[]{150,200,50,100};
        ranking = new int[]{3,2,1,4};
        expectedWinnings = new int[]{0,300,200,0};
        insertPot();
        assertTrue(matches(pot.resolveWinnings(ranking),expectedWinnings));

        moneyInPot= new int[]{150,200,50,100};
        ranking = new int[]{3,4,1,2};
        expectedWinnings = new int[]{100,50,200,150};
        insertPot();
        assertTrue(matches(pot.resolveWinnings(ranking),expectedWinnings));

    }

    @Test
    public void testTiesEvenPot(){

    }

    @Test
    public void testTiesUnevenPot(){

    }
}
