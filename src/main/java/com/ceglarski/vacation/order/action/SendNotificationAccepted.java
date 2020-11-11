package com.ceglarski.vacation.order.action;

import com.ceglarski.vacation.order.VacationOrder;
import com.ceglarski.vacation.order.event.OrderEvent;
import com.ceglarski.vacation.order.state.OrderState;
import lombok.extern.java.Log;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

@Log
public class SendNotificationAccepted implements Action<OrderState, OrderEvent> {

    @Override
    public void execute(StateContext<OrderState, OrderEvent> context) {

        String employee = context.getExtendedState().getVariables().get(VacationOrder.EMPLOYEE_FIELD).toString();

        log.info("\u001B[35m" + "Send notification to employee - " + employee + "\n" +
                "Your vacation request has been accepted :)" + "\u001B[0m");

    }

}