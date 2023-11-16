package com.cardgames.demo.shell;

import com.cardgames.demo.model.GameType;
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
public class GameStatisticShell {

    private final StateMachineService stateMachineService;

    @ShellMethod("Show statistic filtered by gameType")
    public String game(GameType gameType) {
        if (!stateMachineService.isCurrentlyInState(States.STATISTIC)) {
            return null;
        }

        return "There are your statistic filtered by Game Type: \n NOT IMPLEMENTED! \n"; //todo: show user statistic
    }

    @ShellMethod("Go back to game selection")
    public String menu() {
        if (stateMachineService.isCurrentlyInState(States.STATISTIC)) {
            stateMachineService.sendEvent(Events.GO_TO_MENU);
        }
        return "";
    }
}
