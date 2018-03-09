package com.pokerface.pokerapi.game;

import org.springframework.stereotype.Service;

import java.util.List;

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
     * @param gameID represenative of the gamestate in the database
     * @param action the action being performed
     * @return GameUpdateTransport an update for the user to maintain their gamestate
     */
    public GameStateTransport handleAction(long gameID, GameAction action, long userID) {

        GameState gameState = games.findOne(gameID);
        Player player = gameState.getPlayer(userID);
        if (action.getType() == GameActionType.BET) {
            bet(gameState, action, player);
        } else if (action.getType() == GameActionType.FOLD) {
            fold(gameState, action, player);
        } else if (action.getType() == GameActionType.CHECK) {
            check(gameState, action, player);
        }

        if (isHandEnd(gameState)) {
            newHand(gameState);//returns int of winnings, need to hand this out

        } else if (isRoundEnd(gameState, action.getType())) {
            gameState.advanceDealer();
        } else {
            gameState.nextTurn();
        }
        gameState.setLastGameAction(player.getTableSeatID(),action);
        //player.setLastAction(action);
        games.save(gameState);
        GameStateTransport presentGameStateTransport = new GameStateTransport();
        return presentGameStateTransport;
    }

    /**
     * This method handles bet/raise actions
     *
     * @param gameState the state of the game grabbed by repository via ID
     * @param action    the action being performed, interpreted already
     * @param player    the user who performed the action
     * @return the update transport necessary for the clients
     */
    public boolean bet(GameState gameState, GameAction action, Player player) {
        if (player.getCashOnHand() >= action.getBet()) {
            player.setCashOnHand(player.getCashOnHand() - action.getBet()); // remove the bet from the players available cash
            applyBet(gameState, player.getTableSeatID(), action.getBet()); // apply the bet to gamestate
            gameState.setLastBet(player.getTableSeatID()); // update who bet last
            return true;
        } else {
            return false;
            //ERROR TRAPPING NEEDS MORE STATEMENTS, HANDLE NOT ENOUGH BET
        }
    }

    /**
     * check is the method to apply a users check action to the gamestate in question
     *
     * @param gameState the current state of the game being played
     * @param action    the action being applied
     * @param player    the player who is performing the action
     * @return
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
     * @return
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
     * @param gameState
     * @param playerGameID
     * @param bet
     */
    public void applyBet(GameState gameState, int playerGameID, int bet) {
        gameState.matchBet(playerGameID);
        gameState.placeBet(playerGameID, bet);
    }


    /**
     * isRoundEnd returns a boolean value if the Round, the first or second, is at its end as betting has ceased.
     * @param gameState
     * @param actionType
     * @return
     */
    private boolean isRoundEnd(GameState gameState, GameActionType actionType) {
        if (gameState.getPresentTurn() == gameState.getLastBet() && actionType != GameActionType.BET && gameState.getRound()!=3) {
            gameState.setRound(gameState.getRound()+1);
            return true;
        }
        return false;
    }

    private boolean isHandEnd(GameState gameState) {
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
            return true;
        }
        return false;
    }

    /**
     * newHand is called when a game reaches a new round.
     *
     * @param gameState
     */
    private int[] newHand(GameState gameState) {
        int[] winners = new int[gameState.getPlayerCount()];
        winners=gameState.getPot().resolveWinnings(winners);
        Deck deck = gameState.getDeck();
        deck.shuffleDeck();
        for (Player p : gameState.getPlayers()) {
            p.setCardOne(deck.getCard());
            p.setCardTwo(deck.getCard());
        }
        gameState.advanceDealer();
        return winners;
    }

    /**
     * This function adds players, and checks that the starting requirements have been met, thus starting the game.
     *
     * @param playerID
     * @param gameID
     */
    private void addPlayer(long playerID, long gameID) {
        GameState game = games.findOne(gameID);
        if (game.addPlayer(playerID) >= game.minPlayerCount) {
            startGame(game.getId());
        }
        games.save(game);
    }

    /**
     * This function starts games once the amount of users reached is enough. This is done once in the lifetime of a game
     *
     * @param gameID This parameter is the games id being started
     */
    private void startGame(long gameID) {

    }

    /**
     * This method takes a player's ID and finds a game for them. THe logic for this can be improved as matchmaking algorithms are made more complex.
     *
     * @param playerID
     * @return
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
     * @return
     */
    private long createGame() {
        GameState state = new GameState();
        return games.save(state).getId();
    }

    /**
     *
     * @param gameID
     * @param userID
     * @return
     */
    public int getPlayerID(long gameID, long userID) {
        return (games.findOne(gameID).getPlayer(userID)).getTableSeatID();
    }
}
