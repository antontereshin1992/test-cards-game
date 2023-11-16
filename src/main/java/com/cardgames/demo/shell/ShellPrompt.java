package com.cardgames.demo.shell;

import com.cardgames.demo.model.GameType;
import com.cardgames.demo.statemachine.StateMachineService;
import com.cardgames.demo.statemachine.States;
import lombok.RequiredArgsConstructor;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShellPrompt implements PromptProvider {

    private final StateMachineService stateMachineService;

    @Override
    public AttributedString getPrompt() {
        processGameProcessPrompt();
        processInitPrompt();
        processStatisticPrompt();
        processGameSelectionPrompt();

        return new AttributedString("shell>",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    }

    private void processGameProcessPrompt() {
        if (stateMachineService.isCurrentlyInState(States.GAME_PROCESS)) {
            System.out.println("Print can " +
                    "\"fold\": to fold the round, " +
                    "\"leave\": to leave the game, " +
                    "\"showdown\": to show your cards, " +
                    "\"take\": request one more card");
        }
    }

    private void processInitPrompt() {
        if (stateMachineService.isCurrentlyInState(States.INIT)) {
            System.out.println("When you be ready print \"shuffle\" to start game.");
        }
    }

    private void processStatisticPrompt() {
        if (stateMachineService.isCurrentlyInState(States.STATISTIC)) {
            System.out.println("Print \"game GAME_TYPE\": to filter your statistic by game or \"menu\": o go back to the menu or \"quit\": to exit.");
        }
    }

    private void processGameSelectionPrompt() {
        if (stateMachineService.isCurrentlyInState(States.GAME_SELECTION)) {
            String list = Arrays.stream(GameType.values())
                    .map(Enum::name)
                    .sorted()
                    .collect(Collectors.joining(", "));

            System.out.println("Choose one of the game types to start: " + list + ". Print \"play GAME_NAME\" or \"quit\" for exit.");
        }
    }
}
