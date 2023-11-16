package com.cardgames.demo.repository;

import com.cardgames.demo.model.Game;
import com.cardgames.demo.model.GameType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findGameByGameType(GameType gameType);
}
