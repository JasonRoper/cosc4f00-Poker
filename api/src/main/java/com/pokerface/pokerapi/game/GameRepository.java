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
    @Query("SELECT g.id FROM GameState g where g.playerCount<=4")
    List<Long> findOpenGame();

    @Query("SELECT g FROM GameState g where g.startTime IS NOT NULL AND g.startTime<=?1 AND g.hasStarted=FALSE")
    List<GameState> findWaitingToStartGames(long currentTime);


}




