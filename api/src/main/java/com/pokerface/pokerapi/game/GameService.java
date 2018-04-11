package com.pokerface.pokerapi.game;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * GameService is the heart of the logic of the Back End. It communicates solely with the Controller, it handles
 * GameIds and GameAction's to modify the gameState and create and then return Transport objects which are then
 * communicated with the Front End users.
 * <p>
 * It uses a GameRepository which is where it loads and saves GameStates and the changes it has made to them.
 */
@Service
public class GameService {
    private GameRepository games;
    private long AIID=100000000;

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
    public GameState getGameState(long gameID) {

        return games.findOne(gameID);
    }

    /**
     * This method takes the message from the Controller when an action is received, it takes that action, interprets
     * it, gets the gamestate and calls appropriate methods. It also grabs the player reference at the table matching
     * the id and sends it along.
     * <p>
     * After this is completed handleAction then sets that players last action, and advances to the next player.
     *
     * @param gameID   represenative of the gamestate in the database
     * @param action   the action being performed
     * @param playerID the playerID who the action is handling, where they sit
     * @return GameUpdateTransport an update for the user to maintain their gamestate
     */
    public GameStateTransport handleAction(long gameID, GameAction action, int playerID) {

        GameState gameState = games.findOne(gameID);
        gameState.setLastActionTime(System.currentTimeMillis());
        int lastBet=gameState.getLastBet();
        if (gameState.getPresentTurn() == playerID && gameState.isHasStarted()) {
            action.setBet(Math.abs(action.getBet()));
            Player player = gameState.getPlayer(playerID);
            if (action.getType() == GameActionType.BET||action.getType()==GameActionType.RAISE) {
                bet(gameState, action, player);
            } else if (action.getType() == GameActionType.FOLD) {
                fold(gameState, action, player);
            } else if (action.getType() == GameActionType.CHECK||action.getType()==GameActionType.CALL) {
                check(gameState, action, player);
                gameState.setLastBet(lastBet);
            }
            player.updateLastGameAction(action);
            if (onlyPlayerLeftAfterTheyAllIn(playerID,gameState)){

            }
            if (playersStillIn(gameState)) {
                gameState.nextTurn();
            } else {
                gameState.setPreviousTurn(gameState.getPresentTurn());
                for (int i=0;i<30;i++) {
                    gameState.setPresentTurn(gameState.advanceCounter(gameState.getPresentTurn()));
                    if (!gameState.getPlayers().get(gameState.getPresentTurn()).getHasFolded()){
                        break;
                    }
                }
            }

            gameState = games.save(gameState);
        }


        return getGameStateTransport(gameState);
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
        int lastBet=player.getPlayerID();
        if (player.getCashOnHand()<(gameState.getMinimumBet()-player.getBet())){
            lastBet=gameState.getLastBet();
        }
        int amountToBet = action.getBet() + gameState.getMinimumBet() - player.getBet();
        if (amountToBet >= player.getCashOnHand()) {
            allIn(gameState, player);
        } else {
            gameState.placeBet(player, amountToBet);
        }
        gameState.setLastBet(lastBet);
        return true;
    }

    private boolean onlyPlayerLeftAfterTheyAllIn(int playerID,GameState gameState){
        for (Player p:gameState.getPlayers()){
            if (p.getPlayerID()!=playerID){
                if (!p.isAllIn()&&!p.getHasFolded()){
                    return false;
                }
            }
        }
        if (!gameState.getPlayers().get(playerID).getHasFolded()){
            gameState.getPlayers().get(playerID).setAllIn(true);
            return true;
        }
        return true;
    }

    private boolean allIn(GameState gameState, Player player) {
        player.setAllIn(true);
        int amountToBet = player.getCashOnHand();
        gameState.placeBet(player, amountToBet);
        return true;
    }

    /**
     * check is the method to apply a users check action to the gamestate in question
     *
     * @param gameState the current state of the game being played
     * @param action    the action being applied
     * @param player    the player who is performing the action
     * @return boolean representing if it went through successfully
     */
    private boolean check(GameState gameState, GameAction action, Player player) {
        int amountToBet = gameState.getMinimumBet() - player.getBet();
        if (amountToBet >= player.getCashOnHand()) {
            return allIn(gameState, player);
        }
        gameState.placeCheck(player);

        return true;
    }

    /**
     * fold allows the user to fold, surrendering the hand in question
     *
     * @param gameState gameState where the user is folding
     * @param action    the data of the fold
     * @param player    is the one who is folding
     * @return boolean representing if it went through successfully
     */
    private boolean fold(GameState gameState, GameAction action, Player player) {
        if (player.getHasFolded() == false) {
            gameState.getPlayers().get(player.getPlayerID()).setHasFolded(true);
            return true;
        } else {
            return false;
        }

    }


    /**
     * isRoundEnd checks if the game is at the end of the round,
     * this chains to the actual call by getting the game first
     *
     * @param gameID the ID of the game being checked
     * @return a chained boolean of if the round has ended
     */
    public boolean isRoundEnd(long gameID) {
        return isRoundEnd(games.findOne(gameID));
    }

    /**
     * isRoundEnd returns a boolean value if the Round, the first or second, is at its end as betting has ceased.
     *
     * @param gameState of the game being checked
     * @return boolean representing if the round is ended
     */
    public boolean isRoundEnd(GameState gameState) {return !gameState.lastActionBet()&&gameState.getPreviousTurn() == gameState.getLastBet();
    }

    /**
     * isHandEnd is a chain called to isHandEnd with the gameState. THis grabs the gamestate
     *
     * @param gameID the game being grabbed
     * @return boolean return of chain call
     */
    public boolean isHandEnd(long gameID) {
        return isHandEnd(games.findOne(gameID));
    }

    /**
     * isHandEnd checks if a hand has ended, has everyone but one player folded, or are we at the showdown?
     *
     * @param gameState the game being checked
     * @return boolean value representing if the game hand has ended
     */
    public boolean isHandEnd(GameState gameState) {

        return ((isRoundEnd(gameState) && gameState.getRound() == 4)) || !playersStillIn(gameState);

    }

    boolean playersStillIn(GameState gameState){
        int playersFolded=0;
        int playersAllIn=0;
        for (Player p : gameState.getPlayers()){
            if (p.isAllIn()){
                playersAllIn++;
            }else if (p.getHasFolded()){
                playersFolded++;
            }
        }
        if (playersAllIn+playersFolded==gameState.getPlayerCount()||playersFolded==gameState.getPlayerCount()-1){
            return false;
        } else  {
            return true;
        }
    }


    /**
     * This function adds players to the game
     *
     * @param userID UserID of player being added,
     * @param gameID gameID they are being added to
     */
    private void addPlayer(long userID, long gameID, String userName) {
        GameState game = games.findOne(gameID);
        if (!game.hasPlayer(userID)) {
            game.addPlayer(userID,userName);
        }
        game = games.save(game);

    }

    public void addAIPlayer(long gameID){
        GameState game = games.findOne(gameID);
        if (!game.hasPlayer(AIID)){
            game.addAIPlayer(AIID,"AIPlayer"+(AIID-100000000));
        }
        game = games.save(game);
        AIID++;
    }


    /**
     * This method takes a player's ID and finds a game for them. THe logic for this can be improved
     * as matchmaking algorithms are made more complex.
     *
     * @param userID the playerID needing to be added to a game
     * @return the long id of the game they will join
     */
    public long casualMatchmake(long userID, String userName) {
        long gameID = -1; // -1 is never a legitimate gameID, this allows error checking for unfound game.
        gameID = firstAvailableGame(GameState.GameType.CASUAL);
        if (gameID == -1) {
            gameID = createGame(4,GameState.GameType.CASUAL);
            addAIPlayer(gameID);
            addAIPlayer(gameID);
        }
        addPlayer(userID, gameID, userName);

        return gameID;
    }

    public long competitiveMatchmake(long userID, String userName) {
        long gameID = -1; // -1 is never a legitimate gameID, this allows error checking for unfound game.
        gameID = firstAvailableGame(GameState.GameType.COMPETETIVE);
        if (gameID == -1) {
            gameID = createGame(4,GameState.GameType.COMPETETIVE);
        }
        addPlayer(userID, gameID, userName);

        return gameID;
    }

    /**
     * This method is used to find the first available game and return the id of that game, used for matchmaking.
     *
     * @return the ID of the available game
     */
    private long firstAvailableGame(GameState.GameType gameType) {
        List<Long> gameIDs;
        if (gameType== GameState.GameType.COMPETETIVE){
            gameIDs = games.findOpenCompetetiveGame();
        } else if (gameType== GameState.GameType.CASUAL){
            gameIDs = games.findOpenCasualGame();
        } else {
            gameIDs = games.findOpenGame();
        }
        if (gameIDs.isEmpty()) {
            return -1;
        }
        long gameID = gameIDs.get(0);
        return gameID;
    }


    /**
     * This creations a new game and returns the ID of that game once it is saved in the repository
     *
     * @return long id of the game created
     */
    public long createGame() {
        return createGame(4);
    }

    public long createGame(GameSettingTransport gameSettings, long userID, String userName) {
        GameState gameState = new GameState(gameSettings);
        gameState.setGameType(GameState.GameType.CUSTOM);
        gameState = games.save(gameState);
        for (int i = 0; i < gameSettings.aiPlayers; i++) {
            addAIPlayer(gameState.getId());

        }
        addPlayer(userID, gameState.getId(), userName);
        return gameState.getId();
    }

    public long createAIGame(GameSettingTransport gameSettings, long userID, String userName) {
        GameState gameState = new GameState(gameSettings);
        gameState.setGameType(GameState.GameType.AI);
        if (gameSettings.aiPlayers<3){
            gameSettings.aiPlayers=3;
        }
        gameState = games.save(gameState);
        for (int i = 0; i < gameSettings.aiPlayers; i++) {
            addAIPlayer(gameState.getId());

        }
        addPlayer(userID, gameState.getId(), userName);
        gameState.setStartTime(System.currentTimeMillis()+30000);
        games.save(gameState);
        return gameState.getId();
    }

    /**
     * Create game takes settings and creates a game with those settings, returning the ID of that game
     *
     * @param minPlayers the minimum players to start a game
     * @return long ID of the game
     */
    public long createGame(int minPlayers) {
        GameState gameState = new GameState();
        gameState.setMinPlayerCount(minPlayers);
        gameState = games.save(gameState);
        return gameState.getId();
    }

    public long createGame(int minPlayers, GameState.GameType gameType){
        long gameID=createGame(minPlayers);
        GameState gameState=games.findOne(gameID);
        gameState.setGameType(gameType);
        games.save(gameState);
        return gameID;
    }


    /**
     * getPlayerID returns the seat of the player.
     *
     * @param gameID the ID of the game in the repository
     * @param userID the id of the user
     * @return int of where the player is sitting
     */
    public int getPlayerID(long gameID, long userID) {
        return (games.findOne(gameID).getPlayer(userID)).getPlayerID();
    }

    /**
     * determineWInnings is the call to return a transport of a hand being ended
     *
     * @param gameID the gameId to determine
     * @return HandEndTransport an object for the Front End communication
     */
    public HandEndTransport determineWinnings(long gameID) {
        GameState gameState = games.findOne(gameID);
        gameState.fillInCommunityCards();
        int[] winners = new int[gameState.getPlayerCount()];
        int[] winnings = new int[gameState.getPlayerCount()];

        List<Card> communityCards = gameState.receiveCommunityCards();
        List<Pair<Integer, HandRanking>> handRanks = new ArrayList<>();

        for (Player p : gameState.getPlayers()) {
            List<Card> playersCards = new ArrayList<>();
            playersCards.addAll(p.receiveCards());
            playersCards.addAll(communityCards);
            List<Pair<Integer, HandRanking>> hands;
            handRanks.add(Pair.of(p.getPlayerID(), new HandRanking(playersCards)));
        }

        handRanks.sort((a, b) -> a.getSecond().compareTo(b.getSecond()));
        int counter = 1;

        for (int i=handRanks.size()-1;i>=0;i--){
            if (gameState.getPlayers().get(handRanks.get(i).getFirst()).getHasFolded()){
                winners[handRanks.get(i).getFirst()]=10000;
            } else {
                winners[handRanks.get(i).getFirst()]=counter;
                if (i>0){
                    if (!handRanks.get(i).equals(handRanks.get(i-1))){
                        counter++;
                    }
                }
            }
        }


        winnings=gameState.getPot().resolveWinnings(winners);
        gameState.applyWinnings(winnings);
        HandEndTransport handEndTransport = new HandEndTransport(winnings, gameState.getPlayers());
        gameState.endHand();
        games.save(gameState);
        return handEndTransport;
    }

    /**
     * handleRound ensures the round is advanced
     *
     * @param gameID tbe gameID whose round is being advanced
     * @return the GameStateTransport of the round
     */
    public GameStateTransport handleRound(long gameID) {
        GameState gameState = games.findOne(gameID);
        gameState.advanceRound();
        games.save(gameState);
        return getGameStateTransport(gameState);
    }

    /**
     * This is a chain method that takes a gameID to pass on to get a
     * gameStateTransport for helper use of both this and the Controller
     *
     * @param gameID the gameId that needs a statetransport
     * @return GameStateTransport is a communication object
     */
    public GameStateTransport getGameStateTransport(long gameID) {
        GameState gameState = games.findOne(gameID);
        return getGameStateTransport(gameState);
    }

    /**
     * getGameStateTransport returns a GameStateTransport object, representing the gameState for the Front End User
     *
     * @param gameState the gameState that is being turned into a transport
     * @return GameStateTransport object
     */
    public GameStateTransport getGameStateTransport(GameState gameState) {
        GameStateTransport gameStateTransport = new GameStateTransport(gameState);
        return gameStateTransport;
    }

    /**
     * This function returns all gameStates held by the repository and turns them into gameStateTransports
     *
     * @return iterable object containing all GameStatesTransports of all GameStates
     */
    public List<GameStateTransport> getGameStateList() {
        List<GameStateTransport> gameStateTransports = new ArrayList<>();
        Iterable<GameState> gameStates = games.findAll();
        for (GameState g : gameStates) {
            gameStateTransports.add(getGameStateTransport(g));
        }
        return gameStateTransports;
    }

    /**
     * Deletes game from repository, called when the game is completed or abandoned.
     *
     * @param gameID of game to delete
     * @return boolean of if game was found and thus deleted
     */
    public boolean deleteGame(long gameID) {
        if (games.exists(gameID)) {
            games.delete(gameID);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This function exists to remove people who have left the game, or if the game is to be closed.
     *
     * @param gameID the long ID of the game to remove the player
     * @param userID the long ID of the user to be removed
     * @return if it worked, boolean value
     */
    public int removePlayer(long gameID, long userID) {
        GameState gameState=games.findOne(gameID);
        gameState.removePlayer(userID);
        games.save(gameState);
        return gameState.receiveHumanPlayerCount();
    }

    public void removeGame(long gameID){
        GameState test = games.findOne(gameID);
        games.delete(gameID);
    }

    public GameStateTransport gameStart(long gameID) {
        GameState gameState = games.findOne(gameID);
        gameState.startGame();
        games.save(gameState);
        return getGameStateTransport(gameID);
    }

    public List<GameStateTransport> startingGames(){
        List<GameStateTransport> gameStateTransports= new ArrayList<GameStateTransport>();
        List<GameState>gameStates=games.findWaitingToStartGames(System.currentTimeMillis());
        for (GameState g : gameStates){
            g.startGame();
            gameStateTransports.add(getGameStateTransport(g));
        }
        return gameStateTransports;
    }

    public List<Long> startingGameIDs(){
        List<Long> gameIDs = new ArrayList<>();
        gameIDs = games.findWaitingToStartGamesIDs(System.currentTimeMillis());
        return gameIDs;
    }

    public List<Long> getPotentialAIGames(){
        List<GameState> gameStates=new ArrayList<>();
        gameStates=games.findGamesWithPotentialAI();
        List<Long>gameIDs=new ArrayList<>();
        for (GameState g:gameStates){
            if (g.getPlayers().get(g.getPresentTurn()).isAI()){
                gameIDs.add(g.getId());
            }
        }
        return gameIDs;
    }

    public String getGameType(long gameID){
        return games.findOne(gameID).getGameType().toString();
    }

    public int[] calculateRatingChanges(long gameID,HandEndTransport winners){
        GameState gameState = games.findOne(gameID);
        int[] ratingChanges=new int[gameState.getPlayerCount()];
        int biggestWinner;
        for (int i=0;i<ratingChanges.length;i++){
            if (winners.getPlayers()[i].getWinnings()>0){
                ratingChanges[i]=gameState.getPlayerCount()-1;
            } else {
                ratingChanges[i]=-1;
            }
        }


        return ratingChanges;
    }

    public long[] getUserIDsFromGame(long gameID){
        GameState gameState = games.findOne(gameID);
       long[] userIDs = new long[gameState.getPlayerCount()];
       for (int i=0; i<userIDs.length;i++){
               userIDs[i] = gameState.getPlayers().get(i).getUserID();
       }

       return userIDs;
    }

    public HandTransport getHandTransport(long gameID, long userID){
        GameState gameState = games.findOne(gameID);
        if (gameState == null) {
            throw new GameDoesNotExistException(gameID);
        }
        
        Player p = gameState.getPlayer(userID);
        return p == null ? null : new HandTransport(p);
    }

    public long getNumActiveGames() {
        return games.countActiveGames();
    }

    public List<GameInfoTransport> getGameList() {
        List<GameInfoTransport> gameList = new ArrayList<>();
        for (GameState gameState : games.findAll()) {
            gameList.add(new GameInfoTransport(gameState));
        }
        return gameList;
    }

    public boolean isMember(long gameID, long userID) {
        GameState gameState = games.findOne(gameID);
        Player p = gameState.getPlayer(userID);
        return p != null;
    }

    public void setUpNextHand(long gameID){
        GameState gameState=games.findOne(gameID);
        gameState.startGame();
    }

    public boolean isAITurn(long gameID){
        GameState gameState=games.findOne(gameID);
        return gameState.getPlayers().get(gameState.getPresentTurn()).isAI();
    }

    public boolean isGameEnd(long gameID){
        GameState gameState=games.findOne(gameID);
        int inGameCount=0;
        for (Player p:gameState.getPlayers()){
         if (p.getCashOnHand()>0){
             inGameCount++;
         }
        }
        if (inGameCount>1){
            return false;
        }
        return true;
    }

    public boolean onlyAIPlayers(GameState gameState){
        for (Player p:gameState.getPlayers()){
            if (!p.isAI()){
                return false;
            }
        }
        return true;
    }

    public void clearIdleGames(){
        long currentTime=System.currentTimeMillis();
        for (GameState gameState:games.findAll()){
            if (gameState.getLastActionTime()==0){
                gameState.setLastActionTime(currentTime);
                games.save(gameState);
            } else if (gameState.getLastActionTime()<=currentTime-300000){
                deleteGame(gameState.getId());
            } else if (onlyAIPlayers(gameState)){
                deleteGame(gameState.getId());
            }
        }
    }

    public boolean doesGameExist(long gameID){
        GameState gameState=games.findOne(gameID);
        if (gameState==null){
            return false;
        } else {
            return true;
        }
    }

}
