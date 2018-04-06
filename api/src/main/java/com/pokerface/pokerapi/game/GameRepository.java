package com.pokerface.pokerapi.game;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * GameRepository is the database, wired up to an SQL database for storage it uses JAVA SQL for custom queries which
 * the GameService might use. It is an extension of CrudRepository.
 */
public interface GameRepository extends CrudRepository<GameState, Long> {

    /**
     * A custom query which when called returns a list of IDs of games that need a player.
     * @return List of Long ids of games without full players
     */
    @Query("SELECT g.id FROM GameState g where g.playerCount<6 AND g.hasStarted=false")
    List<Long> findOpenGame();

    @Query("SELECT g.id FROM GameState g where g.playerCount<6 AND g.hasStarted=false AND g.gameType=1")
    List<Long> findOpenCasualGame();

    @Query("Select g.id FROM GameState g where g.playerCount<6 AND g.hasStarted=false AND g.gameType=2")
    List<Long> findOpenCompetetiveGame();

    @Query("SELECT g.id FROM GameState g where g.hasStarted=false AND g.playerCount<g.maxPlayers AND g.gameType=0")
    List<Long> findOpenCustomGame();


    @Query("SELECT g FROM GameState g where g.startTime IS NOT NULL AND g.startTime<=?1 AND g.hasStarted=FALSE")
    List<GameState> findWaitingToStartGames(long currentTime);

    @Query("SELECT g.id FROM GameState g where g.startTime IS NOT NULL AND g.startTime<=?1 AND g.hasStarted=FALSE")
    List<Long> findWaitingToStartGamesIDs(long currentTime);

    @Query("SELECT g.id FROM GameState g inner join g.players p where p.userID=?1")
    List<Long> findAllGamesWithUser(long userID);

    @Query("SELECT COUNT(g) FROM GameState g WHERE g.hasStarted = true")
    long countActiveGames();

    @Query("SELECT g FROM GameState g WHERE g.hasStarted=true AND (g.gameType=0 OR g.gameType=1 OR g.gameType=3)")
    List<GameState> findGamesWithPotentialAI();
}




