package com.cardgames.demo.services.impl;

import com.cardgames.demo.model.Game;
import com.cardgames.demo.model.GameType;
import com.cardgames.demo.repository.GameRepository;
import com.cardgames.demo.services.CardsGameServiceV1;
import com.cardgames.demo.services.StatisticServiceV1;
import com.cardgames.demo.statemachine.Events;
import com.cardgames.demo.statemachine.States;
import jakarta.annotation.PostConstruct;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CardsGameServiceV1Impl extends StateMachineListenerAdapter<States, Events> implements CardsGameServiceV1 {

    private static final Random random = new Random();


    private final StatisticServiceV1 statisticServiceV1;
    private final GameRepository gameRepository;

    private final Map<String, Integer> pileScore;
    private final List<String> pile;
    private final List<String> userCards;

    private WinScoreRange winScoreRange;

    public CardsGameServiceV1Impl(
            StatisticServiceV1 statisticServiceV1,
            GameRepository gameRepository) {
        this.statisticServiceV1 = statisticServiceV1;
        this.gameRepository = gameRepository;
        this.userCards = new LinkedList<>();
        //init pile
        HashMap<String, Integer> pile = new HashMap<>();
        //todo: implement the limitation about 4 cards for each type
        // -check the limitation when throws a "DEAL_MORE" event
        // -move game details declaration to Constant class
        pile.put("1", 1);
        pile.put("2", 2);
        pile.put("3", 3);
        pile.put("4", 4);
        pile.put("5", 5);
        pile.put("6", 6);
        pile.put("7", 7);
        pile.put("8", 8);
        pile.put("9", 9);
        pile.put("10", 10);
        pile.put("B", 11);
        pile.put("D", 12);
        pile.put("K", 13);
        pile.put("T", 15);
        this.pileScore = Collections.unmodifiableMap(pile);
        this.pile = List.copyOf(pile.keySet());
    }

    @PostConstruct
    private void init() { //todo: after implementing authorization and init sql scripts
        if (gameRepository.findGameByGameType(GameType.CARDS).isEmpty()) {
            Game game = new Game();
            game.setGameType(GameType.CARDS);
            game.setName(GameType.CARDS.name() + System.currentTimeMillis());
            gameRepository.save(game);
        }
    }

    @Override
    public void transition(Transition<States, Events> transition) {
        super.transition(transition);
        if (transition.getSource() == null) {
            return;
        }

        switch (transition.getSource().getId()) {
            case INIT -> initNewRound();
            case GAME_PROCESS -> processRoundSituation(transition.getTrigger().getEvent());
        }
    }

    private void initNewRound() {
        //todo: restrict permission based on user.penalty score
        userCards.clear();
        //clear user's hand and deal 2 cards
        userCards.add(pile.get(randomInRange(0, pile.size() - 1)));
        userCards.add(pile.get(randomInRange(0, pile.size() - 1)));
        System.out.println("Your cards: " + userCards);

        //init randomized win score range
        int baseValue = pile.size() * 2; //todo: move to constants/config
        int lowBorder = randomInRange(baseValue / 2, baseValue - 2);
        winScoreRange = new WinScoreRange(
                lowBorder,
                randomInRange(lowBorder, baseValue)
        );

        //activate game
        statisticServiceV1.activateGame(GameType.CARDS);
    }

    private void processRoundSituation(Events event) {
        Objects.requireNonNull(winScoreRange, "The game has to be initiated!"); //todo: move this message to properties/constants

        switch (event) {
            case FOLD -> statisticServiceV1.updateStatistic(GameType.CARDS, false);
            case LEAVE -> statisticServiceV1.terminateGame(GameType.CARDS);
            case SHOWDOWN -> {
                // todo: implement competition

                int userPoints = userCards.stream()
                        .mapToInt(pileScore::get)
                        .sum();
                boolean winner = winScoreRange.low() >= userPoints && userPoints <= winScoreRange.high();
                statisticServiceV1.updateStatistic(GameType.CARDS, winner);
                System.out.println("Your score is  " + userPoints + ". You " + (winner ? "win" : "lose")); // todo: use string formatter
            }
            case DEAL_MORE -> { //deal 1 more card
                userCards.add(pile.get(randomInRange(0, pile.size() - 1)));
                System.out.println("Your cards: " + userCards);
            }
        }
    }

    private static int randomInRange(int min, int max) { //todo: move to utility class
        return random.nextInt(max - min) + min;
    }
}

record WinScoreRange(int low, int high) {
}
