package com.cardgames.demo.configuration;

import com.cardgames.demo.statemachine.Events;
import com.cardgames.demo.statemachine.States;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends StateMachineConfigurerAdapter<States, Events> {

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
                .withStates()
                .initial(States.GAME_SELECTION)
                .state(States.INIT)
                .state(States.GAME_PROCESS)
                .state(States.STATISTIC);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(States.GAME_SELECTION).target(States.INIT).event(Events.CARDS) //name of game. Just a quick solution
                .and()
                .withExternal()
                .source(States.INIT).target(States.GAME_PROCESS).event(Events.SHUFFLE)
                //todo: add leave command for INIT->GAME_SELECTION
                // -check all listeners "from INIT" and "to GAME_SELECTION"
                // -check statistic flow
                .and()
                .withExternal()
                .source(States.GAME_PROCESS).target(States.GAME_PROCESS).event(Events.DEAL_MORE)
                .and()
                .withExternal()
                .source(States.GAME_PROCESS).target(States.INIT).event(Events.SHOWDOWN)
                .and()
                .withExternal()
                .source(States.GAME_PROCESS).target(States.STATISTIC).event(Events.LEAVE)
                .and()
                .withExternal()
                .source(States.GAME_PROCESS).target(States.INIT).event(Events.FOLD)
                .and()
                .withExternal()
                .source(States.STATISTIC).target(States.GAME_SELECTION).event(Events.GO_TO_MENU);
    }
}