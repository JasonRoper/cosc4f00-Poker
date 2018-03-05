package com.pokerface.pokerapi.game;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<GameState, Long> {

}




