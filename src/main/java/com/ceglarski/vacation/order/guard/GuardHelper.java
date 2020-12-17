package com.ceglarski.vacation.order.guard;

import com.ceglarski.vacation.order.event.OrderEvent;
import com.ceglarski.vacation.order.state.OrderState;
import org.springframework.statemachine.StateContext;

import java.util.HashMap;

class GuardHelper {

    static boolean doesSupervisorAccepted(StateContext<OrderState, OrderEvent> context) {

        boolean doesSupervisorAccepted = false;
        if(context.getMessage().getHeaders().containsKey("data")) {
            HashMap data = context.getMessage().getHeaders().get("data", HashMap.class);
            if(data != null && data.containsKey("response")) {
                doesSupervisorAccepted = (Boolean) data.get("response");
            }
        }
        return doesSupervisorAccepted;
    }

}
