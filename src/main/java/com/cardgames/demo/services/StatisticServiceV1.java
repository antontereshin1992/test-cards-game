package com.cardgames.demo.services;

import com.cardgames.demo.model.GameType;

public interface StatisticServiceV1 {
    void initGame(GameType gameType);

    void updateStatistic(GameType gameType, boolean winner);

    void terminateGame(GameType gameType);

    void activateGame(GameType gameType);
}
