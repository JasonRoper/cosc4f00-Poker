package com.pokerface.pokerapi.game;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AIService {
    GameRepository games;

    public AIService(){

    }
    public AIService(GameRepository gameRepository) {
        this.games = gameRepository;
    }

    /**
     * This receives a GameState, it take the players turn and makes a decision based on its heuristics, it then sends its action to the controller as if it were a player.
     */
    public GameAction playAction(long gameID){
        GameState gameState;
        gameState=games.findOne(gameID);
        int playerNumber=gameState.getPresentTurn();
        int roll;
        Random rand = new Random(System.currentTimeMillis());
            roll=rand.nextInt(10);
            if (roll==0){
                return fold(gameState,playerNumber);
            } else if (roll>0&&roll<7) {
                return check(gameState,playerNumber);
            } else if (roll==7||roll==8) {
                return raise(gameState,playerNumber);
            } else if (roll==9) {
                return allIn(gameState,playerNumber);
            }
            return check(gameState,playerNumber);
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
    private GameAction fold(GameState gameState,int playerNumber){
        GameAction gameAction;

        gameAction=new GameAction(GameActionType.FOLD,0);

        return gameAction;
    }

    /**
     * Performs the check action, may consider other actions and decide to do another action instead
     */
    private GameAction check(GameState gameState,int playerNumber){
        GameAction gameAction;

        gameAction=new GameAction(GameActionType.CHECK,0);

        return gameAction;
    }

    /**
     * Performs the raise action, the amount may be determined by other factors
     */
    private GameAction raise(GameState gameState,int playerNumber){
        GameAction gameAction;

        gameAction=new GameAction(GameActionType.RAISE,gameState.getBigBlind()/2);

        return gameAction;
    }

    /**
     * Decides to go all in, may be called if the AI doesn't have enough cash to check or raise
     */
    private GameAction allIn(GameState gameState,int playerNumber){
        GameAction gameAction;

        gameAction=new GameAction(GameActionType.RAISE, gameState.getPlayers().get(playerNumber).getCashOnHand());

        return gameAction;
    }

    public boolean isAIPlayer(long gameID,int playerID){
      return games.findOne(gameID).getPlayer(playerID).isAI();
    }
}
