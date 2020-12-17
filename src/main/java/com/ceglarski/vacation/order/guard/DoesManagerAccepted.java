package com.ceglarski.vacation.order.guard;

import com.ceglarski.vacation.order.event.OrderEvent;
import com.ceglarski.vacation.order.state.OrderState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public class DoesManagerAccepted implements Guard<OrderState, OrderEvent> {

    @Override
    public boolean evaluate(StateContext<OrderState, OrderEvent> context) {

        return GuardHelper.doesSupervisorAccepted(context);
    }
}
