package com.pokerface.pokerapi.game;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GameRepository extends CrudRepository<GameState, Long> {


    @Query("SELECT g.id FROM GameState g where g.playerCount<=4")
    List<Long> findOpenGame();


}




