package com.ceglarski.vacation.order.action;

import com.ceglarski.vacation.order.VacationOrder;
import com.ceglarski.vacation.order.event.OrderEvent;
import com.ceglarski.vacation.order.state.OrderState;
import lombok.extern.java.Log;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

@Log
public class SendQuestion implements Action<OrderState, OrderEvent> {

    @Override
    public void execute(StateContext<OrderState, OrderEvent> context) {

        String supervisor = getSupervisor(context);
        log.info("\u001B[35m" + "Send question to: " + supervisor + "\u001B[0m");
    }

    private String getSupervisor(StateContext<OrderState, OrderEvent> context) {
        if(context.getSource().getId().equals(OrderState.ASK_LEADER)) {
            return context.getExtendedState().getVariables().get(VacationOrder.LEADER_FIELD).toString();
        } else{
            return context.getExtendedState().getVariables().get(VacationOrder.MANAGER_FIELD).toString();
        }
    }
}
