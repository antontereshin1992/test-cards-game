package com.cardgames.demo.services;

import com.cardgames.demo.model.GameType;

public interface UserServiceV1 {
    void updateUserStatistic(GameType gameType, boolean winner);

    void applyPenalty(GameType gameType);

    Long getActiveUserId();
}
