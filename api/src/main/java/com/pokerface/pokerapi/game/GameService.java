package com.pokerface.pokerapi.game;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * GameService is the heart of the logic of the Back End. It communicates solely with the Controller, it handles GameIds and GameAction's to modify the gameState and create and then return Transport objects which are then communicated with the Front End users.
 *
 * It uses a GameRepository which is where it loads and saves GameStates and the changes it has made to them.
 */
@Service
public class GameService {
    GameRepository games;

    /**
     * This constructor grabs the repository for access to the database
     *
     * @param gameRepository the database and the ability to access the database
     */
    public GameService(GameRepository gameRepository) {
        this.games = gameRepository;
    }

    /**
     * This gets a gamestate transport object for the client when they first join or need it, requiring only the id
     *
     * @param gameID is the representative of the gamestate in the database
     * @return the object for communication to the frontend
     */
    public GameStateTransport getGameState(long gameID) {
        games.findOne(gameID);
        return null;
    }

    /**
     * This method takes the message from the Controller when an action is received, it takes that action, interprets it, gets the gamestate and calls appropriate methods. It also grabs the player reference at the table matching the id and sends it along.
     *
     * After this is completed handleAction then sets that players last action, and advances to the next player.
     *
     * @param gameID represenative of the gamestate in the database
     * @param action the action being performed
     * @return GameUpdateTransport an update for the user to maintain their gamestate
     */
    public GameStateTransport handleAction(long gameID, GameAction action, int playerID) {

        GameState gameState = games.findOne(gameID);
        Player player = gameState.getPlayer(playerID);
        if (action.getType() == GameActionType.BET) {
            bet(gameState, action, player);
        } else if (action.getType() == GameActionType.FOLD) {
            fold(gameState, action, player);
        } else if (action.getType() == GameActionType.CHECK) {
            check(gameState, action, player);
        }

        gameState.setLastGameAction(player.getTableSeatID(),action);
        gameState.nextTurn();
        games.save(gameState);
        GameStateTransport presentGameStateTransport = getGameStateTransport(gameState);

        return presentGameStateTransport;
    }

    /**
     * This method handles bet/raise actions applying a bet
     *
     * @param gameState the state of the game grabbed by repository via ID
     * @param action    the action being performed, interpreted already
     * @param player    the user who performed the action
     * @return boolean representing if it went through successfully
     */
    public boolean bet(GameState gameState, GameAction action, Player player) {
        if (player.getCashOnHand() >= action.getBet()) {
            player.setCashOnHand(player.getCashOnHand() - action.getBet()); // remove the bet from the players available cash
            applyBet(gameState, player.getTableSeatID(), action.getBet()); // apply the bet to gamestate
            gameState.setLastBet(player.getTableSeatID()); // update who bet last
            return true;
        } else {
            return false;
        }
    }

    /**
     * check is the method to apply a users check action to the gamestate in question
     *
     * @param gameState the current state of the game being played
     * @param action    the action being applied
     * @param player    the player who is performing the action
     * @return boolean representing if it went through successfully
     */
    public boolean check(GameState gameState, GameAction action, Player player) {
        if (player.getCashOnHand() >= gameState.getMinimumBet() - gameState.getPot().getBet(player.getTableSeatID())) {
            player.setCashOnHand(player.getCashOnHand() - gameState.getMinimumBet() - gameState.getPot().getBet(player.getTableSeatID()));
            return true;
        } else {
            return false;
        }

    }

    /**
     * fold allows the user to fold, surrendering the hand in question
     *
     * @param gameState
     * @param action
     * @param player
     * @return boolean representing if it went through successfully
     */
    public boolean fold(GameState gameState, GameAction action, Player player) {
        if (player.getHasFolded()==false) {
            gameState.getPlayers().get(player.getTableSeatID()).setHasFolded(true);
            return true;
        } else {
            return false;
        }

    }

    /**
     * ApplyBet takes a players bet and applies it to the gameState
     *
     * @param gameState the game being bet on
     * @param playerGameID the id of the player betting
     * @param bet the bet being applied
     */
    public void applyBet(GameState gameState, int playerGameID, int bet) {
        gameState.matchBet(playerGameID);
        gameState.placeBet(playerGameID, bet);
    }

    /**
     * isRoundEnd checks if the game is at the end of the round, this chains to the actual call by getting the game first
     * @param gameID the ID of the game being checked
     * @return a chained boolean of if the round has ended
     */
    public boolean isRoundEnd(long gameID){
        return isRoundEnd(games.findOne(gameID));
    }

    /**
     * isRoundEnd returns a boolean value if the Round, the first or second, is at its end as betting has ceased.
     * @param gameState of the game being checked
     * @return boolean representing if the round is ended
     */
    public boolean isRoundEnd(GameState gameState) {
        if (gameState.getPresentTurn() == gameState.getLastBet() && gameState.getLastGameActions().get(gameState.getPreviousTurn()).getType() != GameActionType.BET && gameState.getRound()!=3) {
            return true;
        }
        return false;
    }

    /**
     * isHandEnd is a chain called to isHandEnd with the gameState. THis grabs the gamestate
     * @param gameID the game being grabbed
     * @return boolean return of chain call
     */
    public boolean isHandEnd(long gameID){
        return isHandEnd(games.findOne(gameID));
    }

    /**
     * isHandEnd checks if a hand has ended, has everyone but one player folded, or are we at the showdown?
     * @param gameState the game being checked
     * @return boolean value representing if the game hand has ended
     */
    public boolean isHandEnd(GameState gameState) {
        int notFolded = 0;
        for (Player p : gameState.getPlayers()) {
            if (!p.getHasFolded()) {
                notFolded++;
            }
        }
        if (notFolded == 1) {
            if (gameState.getRound() == 3) {
                return false;
            }
            gameState.advanceDealer();
            return true;
        }
        return false;
    }

    /**
     * This function adds players to the game
     *
     * @param userID UserID of player being added,
     * @param gameID gameID they are being added to
     */
    private void addPlayer(long userID, long gameID) {
        GameState game = games.findOne(gameID);
game.addPlayer(userID);
        games.save(game);
    }


    /**
     * This method takes a player's ID and finds a game for them. THe logic for this can be improved as matchmaking algorithms are made more complex.
     *
     * @param playerID the playerID needing to be added to a game
     * @return the long id of the game they will join
     */
    public long matchmake(long playerID) {
        long gameID = 0; // 0 is never a legitimate gameID, this allows error checking for unfound game.
        gameID = firstAvailableGame();
        if (gameID == 0) {
            gameID = createGame();
        }
        return gameID;
    }

    /**
     * This method is used to find the first available game and return the id of that game, used for matchmaking.
     *
     * @return the ID of the available game
     */
    private long firstAvailableGame() {
        List<Long> gameIDs;
        gameIDs = games.findOpenGame();
        if (gameIDs.isEmpty()) {
            return 0;
        }
        long gameID = gameIDs.get(0);
        return gameID;
    }


    /**
     * This creations a new game and returns the ID of that game once it is saved in the repository
     *
     * @return long id of the game created
     */
    private long createGame() {
        GameState state = new GameState();
        return games.save(state).getId();
    }

    /**
     * getPlayerID returns the seat of the player.
     * @param gameID the ID of the game in the repository
     * @param userID the id of the user
     * @return int of where the player is sitting
     */
    public int getPlayerID(long gameID, long userID) {
        return (games.findOne(gameID).getPlayer(userID)).getTableSeatID();
    }

    /**
     * determineWInnings is the call to return a transport of a hand being ended
     * @param gameID the gameId to determine
     * @return HandEndTransport an object for the Front End communication
     */
    public HandEndTransport determineWinnings(long gameID){
        GameState gameState = games.findOne(gameID);
        int[] winners = new int[gameState.getPlayerCount()];
        //DETERMINE WINNERS HERE
        winners=gameState.getPot().resolveWinnings(winners);
        HandEndTransport handEndTransport = new HandEndTransport(winners,gameState.getPlayers());
        return handEndTransport;
    }

    /**
     * handleRound ensures the round is advanced
     * @param gameID
     * @return
     */
    public GameStateTransport handleRound(long gameID){
        GameState gameState = games.findOne(gameID);
        games.save(gameState);
        return getGameStateTransport(gameState);
    }

    /**
     * This is a chain method that takes a gameID to pass on to get a gameStateTransport for helper use of both this and the Controller
     * @param gameID
     * @return GameStateTransport is a communication object
     */
    public GameStateTransport getGameStateTransport(long gameID){
        GameState gameState = games.findOne(gameID);
        return getGameStateTransport(gameState);
    }

    /**
     * getGameStateTransport returns a GameStateTransport object, representing the gameState for the Front End User
     * @param gameState
     * @return GameStateTransport object
     */
    public GameStateTransport getGameStateTransport(GameState gameState){
        GameStateTransport gameStateTransport = new GameStateTransport(gameState.getCommunityCardOne(),gameState.getCommunityCardTwo(),gameState.getCommunityCardThree(),gameState.getCommunityCardFour(),gameState.getCommunityCardFive(),gameState.getPot().getSum(),gameState.getBigBlind(),null,null,gameState.getPlayers(),gameState.getLastGameActions(),gameState.getPresentTurn());
return gameStateTransport;
    }
    //public GameStateTransport(Card[] communityCards, int potSum, int bigBlind, Reason action, String event,Player[] players,List<GameAction> gameActions,int nextPlayer){

    }
