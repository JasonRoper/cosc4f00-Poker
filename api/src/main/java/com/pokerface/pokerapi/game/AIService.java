package com.pokerface.pokerapi.game;

import java.util.Random;

public class AIService {
    GameState gameState;

    public AIService(){

    }

    /**
     * This receives a GameState, it take the players turn and makes a decision based on its heuristics, it then sends its action to the controller as if it were a player.
     */
    public void playAction(GameState gameState){
        this.gameState=gameState;
        int playerNumber=gameState.getPresentTurn();
        int roll;
        Random rand = new Random(System.currentTimeMillis());
            roll=rand.nextInt(10);
            if (roll==0){
                fold();
            } else if (roll>0&&roll<7) {
                check();
            } else if (roll==7||roll==8) {
                raise();
            } else if (roll==9) {
                allIn();
            }
    }

    /**
     * Provides the ability to consider how good a hand is. This will influence the reality of what actions are actually chosen.
     * @return an integer value from 1-10 that gives the value of the hand
     */
    private int handEvaluation(){

        return 0;
    }

    /**
     * Performs the action of folding, will consider other factors to see if it really wishes to fold
     */
    private void fold(){

    }

    /**
     * Performs the check action, may consider other actions and decide to do another action instead
     */
    private void check(){

    }

    /**
     * Performs the raise action, the amount may be determined by other factors
     */
    private void raise(){

    }

    /**
     * Decides to go all in, may be called if the AI doesn't have enough cash to check or raise
     */
    private void allIn(){

    }
}
