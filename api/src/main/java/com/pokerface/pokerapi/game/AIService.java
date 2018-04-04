package com.pokerface.pokerapi.game;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The AIService exists within the GameService as an object. When the GameController, using the GameService, detects
 * that the next player in a given GameState is an AI Player who has not folded it sends the message to the
 * AIService to wake up to determines its turn. It then sends this turn as if it were a player and the GameService
 * handles it like any other player action.
 * <p>
 *
 * The AI requires only the GameState to determine its actions, it uses information that would be available to any
 * other player. It knows its position, how much it has bet, what its cards are. The AI uses a random number
 * generated by a Random object whose seed is created when an action is needed by the AI. This seed is generated by
 * the current system time in milliseconds. It generates an int from 0, and from that using if statements one of
 * four actions is chose: Fold, Check, Raise, All-In
 *<p>
 *
 *  Each action is wrapped into a method that is called by the if statement evaluation. At this point further
 *  heuristical analysis is done to refine the AI Move. First, the move is checked to ensure it is a legal action,
 *  IE the AI does not bet more than it has, or go all in if it cannot even match the bet.
 *<p>
 *
 *  For a fold, the AI does a check on its hand value to see if it would be reckless to fold if it had a superior
 *  hand. If a check is called, it does so, this may result in an all in as it does not have enough to continue
 *  playing otherwise. If a bet is called, it will examine its hand to see if it is a small trivial increase
 *  (weighted against the big blind and current pot size) or a larger one, potentially an all-in if its hand is
 *  quite good. If it is an all-in, it will weight the final chance of this action against its hand value.
 *<p>
 *
 *  None of these examples are final or deterministic, in Poker it is vital to play not just the odds perfectly,
 *  but to have a measure of unpredictability, and as such while the weights may be effected, each decision is made
 *  stochastically using the Random object defined above.
 */
@Service
public class AIService {
    GameRepository games;
    HandRanking handRanking;
    HandRanking lowPair;

    /**
     * The {@link AIService} depends on the {@link GameRepository}.
     *
     * @param gameRepository the {@link GameRepository} to use.
     */
    public AIService(GameRepository gameRepository) {
        this.games = gameRepository;
    }

    /**
     * This receives a GameState, it take the players turn and makes a decision based on its heuristics, it then sends its action to the controller as if it were a player.
     *
     * @param gameID the gameID needing an action on
     * @return the GameAction the ai has decided to perform
     */
    public GameAction playAction(GameService gameService,long gameID){
        GameState gameState=gameService.getGameState(gameID);
        lowPair=createLowPair();
        List<Card> cards = new ArrayList<>();
        cards.addAll(gameState.getPlayers().get(gameState.getPresentTurn()).receiveCards());
        cards.addAll(gameState.receiveCommunityCards());
        handRanking=new HandRanking(cards);
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
     * The fold action has a double check to see if its hand is worth keeping in play
     * @param gameState a gameState object to folding
     * @param playerNumber the user who is folding
     * @return GameAction
     */
    private GameAction fold(GameState gameState,int playerNumber){
        GameAction gameAction;

        gameAction=new GameAction(GameActionType.FOLD,0);

        return gameAction;
    }


    /**
     * check returns a check action, it is a safe game continuing action that keeps the game in play.
     * @param gameState the gameState being done
     * @param playerNumber the playerNumber performing the check
     * @return the GameAction being performed
     */
    private GameAction check(GameState gameState,int playerNumber){
        GameAction gameAction;

        gameAction=new GameAction(GameActionType.CHECK,0);

        return gameAction;
    }

    /**
     * Takes the gameState and sees if the AI should apply a raise, making sure it has enough money
     * @param gameState the gameState the action is being applied to
     * @param playerNumber the playerID at that game
     * @return the action being performed
     */
    private GameAction raise(GameState gameState,int playerNumber){
    GameAction gameAction = new GameAction(GameActionType.RAISE, gameState.getBigBlind() / 2);

        return gameAction;
    }

    /**
     * AllIn bets all of the AI's money, done rarely, more often with a better hand but also out of the blue sometimes.
     * raise function can call this if they do not have enough money to bet, to ake sure it's handle properly.
     * @param gameState the gameState they are all inning on
     * @param playerNumber their seat at that table
     * @return the action being performed
     */
    private GameAction allIn(GameState gameState,int playerNumber){
        GameAction gameAction;
        if (handRanking.compareTo(lowPair)<1){
            return raise(gameState,playerNumber);
        }
        gameAction=new GameAction(GameActionType.RAISE, gameState.getPlayers().get(playerNumber).getCashOnHand());

        return gameAction;
    }

    /**
     * isAPlayer lets the controller know if a player in a game is an AI.
     * @param gameID the game to be checked
     * @param playerID the player in the seat being checked
     * @return true if it is an ai, false otherwise
     */
    public boolean isAIPlayer(long gameID,int playerID){
      return games.findOne(gameID).getPlayer(playerID).isAI();
    }

    public HandRanking createLowPair(){
        List<Card> hand = new ArrayList<>();
        hand.add(Card.CLUBS_EIGHT);
        hand.add(Card.SPADES_EIGHT);
        return new HandRanking(hand);
    }
}
