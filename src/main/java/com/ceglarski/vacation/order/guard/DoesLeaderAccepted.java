package com.ceglarski.vacation.order.guard;

import com.ceglarski.vacation.order.event.OrderEvent;
import com.ceglarski.vacation.order.state.OrderState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

import java.util.HashMap;

public class DoesLeaderAccepted implements Guard<OrderState, OrderEvent> {

    @Override
    public boolean evaluate(StateContext<OrderState, OrderEvent> context) {

        boolean doesLeaderAccepted = false;
        if(context.getMessage().getHeaders().containsKey("data")) {
            HashMap data = context.getMessage().getHeaders().get("data", HashMap.class);
            if(data != null && data.containsKey("response")) {
                doesLeaderAccepted = (Boolean) data.get("response");
            }
        }
        return doesLeaderAccepted;
    }
}
