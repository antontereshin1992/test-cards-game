package com.cardgames.demo.shell;

import com.cardgames.demo.model.GameType;
import com.cardgames.demo.statemachine.StateMachineService;
import com.cardgames.demo.statemachine.Events;
import com.cardgames.demo.statemachine.States;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
@Slf4j
public class GameSelectionShell {

    private final ConfigurableApplicationContext context;
    private final StateMachineService stateMachineService;

    @ShellMethod("Choose the game type to start.")
    public String play(String gameName) {
        if (!stateMachineService.isCurrentlyInState(States.GAME_SELECTION)) {
            return null;
        }

        return processPlayCommand(gameName);
    }

    @ShellMethod("Close the app.")
    public void quit() {
        if (stateMachineService.isCurrentlyInState(States.GAME_SELECTION)
                || stateMachineService.isCurrentlyInState(States.STATISTIC)) {
            context.close();
        }
    }

    private String processPlayCommand(String gameName) {
        try {
            GameType gameType = GameType.valueOf(gameName.toUpperCase());
            if (gameType == GameType.CARDS) {
                stateMachineService.sendEvent(Events.CARDS);
                return "";
            } else {
                System.out.println("The game isn't implemented yet.");
            }
        } catch (Exception e) {
            System.out.println("Entered game doesn't exist. Please try again.");
        }
        return null;
    }
}
