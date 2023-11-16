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
public class CardGameShuffleShell {

    private final StateMachineService stateMachineService;

    @ShellMethod("Shuffle cards")
    public String shuffle() {
        if (!stateMachineService.isCurrentlyInState(States.INIT)) {
            return null;
        }

        stateMachineService.sendEvent(Events.SHUFFLE);
        return "OK! Here you are your cards.";
    }
}
