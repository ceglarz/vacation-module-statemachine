package com.ceglarski.vacation.order;

import lombok.extern.java.Log;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

@Log
public class StateMachineListener extends StateMachineListenerAdapter {

    @Override
    public void transition(Transition transition) {
        log.info(String.format("Transitioned: %s -> %s%n", transition.getSource().getId(), transition.getTarget().getId()));
    }

    @Override
    public void stateEntered(State state) {
        log.info("State: " + state.getId());
    }
}