package com.pokerface.pokerapi.game;

import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<GameState, Long> {
}
