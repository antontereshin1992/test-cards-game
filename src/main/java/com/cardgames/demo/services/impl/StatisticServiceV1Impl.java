package com.cardgames.demo.services.impl;

import com.cardgames.demo.exceprion.NotFoundException;
import com.cardgames.demo.model.GameStatus;
import com.cardgames.demo.model.GameType;
import com.cardgames.demo.model.Statistic;
import com.cardgames.demo.repository.GameRepository;
import com.cardgames.demo.repository.StatisticRepository;
import com.cardgames.demo.services.StatisticServiceV1;
import com.cardgames.demo.services.UserServiceV1;
import com.cardgames.demo.statemachine.Events;
import com.cardgames.demo.statemachine.States;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StatisticServiceV1Impl extends StateMachineListenerAdapter<States, Events> implements StatisticServiceV1 {

    private final UserServiceV1 userServiceV1;
    private final StatisticRepository statisticRepository;
    private final GameRepository gameRepository;

    @PostConstruct
    private void init() {
        statisticRepository.deleteAll(); // todo: remove after processing exceptions
    }

    @Override
    public void transition(Transition<States, Events> transition) {
        super.transition(transition);
        if (transition.getTarget() == null) {
            return;
        }

        if (transition.getTarget().getId() == States.INIT) {
            processGameSelection(transition.getTrigger().getEvent());
        }
    }

    @Override
    public void initGame(GameType gameType) {
        Statistic statistic = new Statistic();
        statistic.setGame(gameRepository.findGameByGameType(gameType).orElseThrow()); //todo: throw business exception
        statistic.setStatus(GameStatus.SCHEDULED);
        statisticRepository.save(statistic);
    }

    @Override
    @Transactional
    public void updateStatistic(GameType gameType, boolean winner) {
        Statistic statistic = getActiveGameStatistic(gameType);

        statistic.setStatus(GameStatus.FINISHED);
        if (winner) {
            statistic.setWinnerId(userServiceV1.getActiveUserId());
        }
        statisticRepository.save(statistic);
        userServiceV1.updateUserStatistic(gameType, winner);
    }

    @Override
    @Transactional
    public void terminateGame(GameType gameType) {
        Statistic statistic = getActiveGameStatistic(gameType);
        statistic.setStatus(GameStatus.TERMINATED);
        statisticRepository.save(statistic);
        userServiceV1.applyPenalty(GameType.CARDS);
    }

    @Override
    public void activateGame(GameType gameType) {
        Statistic statistic = getScheduledGameStatistic(gameType);
        statistic.setStatus(GameStatus.ACTIVE);
        statisticRepository.save(statistic);
    }

    private Statistic getActiveGameStatistic(GameType gameType) {
        return statisticRepository.findStatisticByStatus(GameStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Cound't find any active game with type " + gameType));
    }

    private Statistic getScheduledGameStatistic(GameType gameType) {
        return statisticRepository.findStatisticByStatus(GameStatus.SCHEDULED)
                .orElseThrow(() -> new NotFoundException("Cound't find any scheduled game with type " + gameType));
    }

    private void processGameSelection(Events events) {
        switch (events) {
            case CARDS, SHOWDOWN, FOLD -> initGame(GameType.CARDS); // todo: review after implementing one more game
            default -> {/*NOP*/}
        }
    }
}
