package com.cardgames.demo.statemachine;

import jakarta.annotation.PostConstruct;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public final class StateMachineService {

    private final StateMachine<States, Events> stateMachine;
    private final Collection<StateMachineListener> stateMachineListeners;

    public StateMachineService(StateMachine<States, Events> stateMachine,
                               Collection<StateMachineListener> stateMachineListeners) {

        this.stateMachineListeners = stateMachineListeners;
        this.stateMachine = stateMachine;
    }

    @PostConstruct
    private void init() {
        stateMachineListeners.forEach(stateMachine::addStateListener);
        stateMachine.start();
    }

    public boolean isCurrentlyInState(States state) {
        State<States, Events> current = stateMachine.getState();
        return current == null || current.getId().equals(state);
    }

    public void sendEvent(Events event) {
        stateMachine.sendEvent(event);
    }
}
