package com.pokerface.pokerapi.game;

import org.springframework.stereotype.Service;

@Service
public class GameService {
    GameRepository gameRepository;

    /**
     * This constructor grabs the repository for access to the database
     * @param gameRepository the database and the ability to access the database
     */
    public GameService(GameRepository gameRepository){
        this.gameRepository=gameRepository;
    }

    /**
     * This gets a gamestate transport object for the client when they first join or need it, requiring only the id
     * @param id is the representative of the gamestate in the database
     * @return the object for communication to the frontend
     */
    public GameStateTransport getGameState(long id){

        return null;
    }

    /**
     * This method takes the message from the Controller when an action is received, it takes that action, interprets it, gets the gamestate and calls appropriate methods. It also grabs the player reference at the table matching the id and sends it along.
     * @param id represenative of the gamestate in the database
     * @param action the action being performed
     * @return GameUpdateTransport an update for the user to maintain their gamestate
     */
    public GameUpdateTransport handleAction(long id, GameAction action, long userID){
        return null;


//        Player player=null;
//        for (int i=0;i<gameState.players.size();i++){
//            if (gameState.players.get(i).getId()==userID){
//                player=gameState.players.get(i);
//                break;
//            }
//        }
    }

    /**
     * This method handles bet/raise actions
     * @param gameState the state of the game grabbed by repository via ID
     * @param action the action being performed, interpreted already
     * @param player the user who performed the action
     * @return the update transport necessary for the clients
     */
    public GameUpdateTransport bet (GameState gameState, GameAction action, Player player){

        if (player.getCashOnHand()>=action.getBet()){
            player.setCashOnHand(player.getCashOnHand()-action.getBet()); // remove the bet from the players available cash
            applyBet(gameState,player.getTableSeatID(),action.getBet()); // apply the bet to gamestate
            gameState.setLastBet(player.getTableSeatID()); // update who bet last
        } else {
            //ERROR TRAPPING NEEDS MORE STATEMENTS, HANDLE NOT ENOUGH BET
        }

        return null;
    }

    /**
     * check is the method to apply a users check action to the gamestate in question
     * @param gameState the current state of the game being played
     * @param action the action being applied
     * @param player the player who is performing the action
     * @return
     */
    public GameUpdateTransport check (GameState gameState, GameAction action, Player player){
        return null;
    }

    /**
     * fold allows the user to fold, surrendering the hand in question
     * @param gameState
     * @param action
     * @param player
     * @return
     */
    public GameUpdateTransport fold (GameState gameState, GameAction action, Player player){
        return null;
    }

    public void applyBet(GameState gameState, int playerGameID, int bet) {
        //NEEDS TO BE FILLED IN, handle application to pot, and the minimum bet required
    }

}
