package com.pokerface.pokerapi.game;

import java.util.Random;

public class AIService {

    public AIService(){

    }

    /**
     * This receives a GameState, it take the players turn and makes a decision based on its heuristics, it then sends its action to the controller as if it were a player.
     */
    public void playAction(GameState gameState){
        int playerNumber=gameState.getPresentTurn();
        int roll;
        Random rand = new Random(System.currentTimeMillis());
        while(true){
            roll=rand.nextInt(10);
            if (roll==0){
                //fold
            } else if (roll>0&&roll<7) {
                //check
            } else if (roll==7||roll==8) {
                //raise
            } else if (roll==9) {
                //all in
            }

        }
    }
}
