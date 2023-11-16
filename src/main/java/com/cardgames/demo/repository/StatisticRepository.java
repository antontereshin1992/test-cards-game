package com.cardgames.demo.repository;

import com.cardgames.demo.model.GameStatus;
import com.cardgames.demo.model.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    Optional<Statistic> findStatisticByStatus(GameStatus gameStatus); //todo: implement timestamp and search by Id
}
