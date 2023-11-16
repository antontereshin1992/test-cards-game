package com.cardgames.demo.shell.card;

import com.cardgames.demo.statemachine.StateMachineService;
import com.cardgames.demo.statemachine.Events;
import com.cardgames.demo.statemachine.States;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
@Slf4j
public class CardGameProcessShell {

    private final StateMachineService stateMachineService;

    @ShellMethod("Fold the round")
    public String fold() {
        if (!stateMachineService.isCurrentlyInState(States.GAME_PROCESS)) {
            return null;
        }

        stateMachineService.sendEvent(Events.FOLD);
        return "You lose. Once more?";
    }

    @ShellMethod("Leave the game")
    public String leave() {
        if (!stateMachineService.isCurrentlyInState(States.GAME_PROCESS)) {
            return null;
        }

        stateMachineService.sendEvent(Events.LEAVE);
        return "Now you can see your statistic:";
    }

    @ShellMethod("Show your cards")
    public String showdown() {
        if (!stateMachineService.isCurrentlyInState(States.GAME_PROCESS)) {
            return null;
        }

        stateMachineService.sendEvent(Events.SHOWDOWN);
        return ""; //todo: print user's cards
    }

    @ShellMethod("Take one more card")
    public String take() {
        if (!stateMachineService.isCurrentlyInState(States.GAME_PROCESS)) {
            return null;
        }

        stateMachineService.sendEvent(Events.DEAL_MORE);
        return "OK! Here you are one more card.";
    }
}
