package com.ceglarski.vacation.order.action;

import com.ceglarski.vacation.order.VacationOrder;
import com.ceglarski.vacation.order.event.OrderEvent;
import com.ceglarski.vacation.order.state.OrderState;
import lombok.extern.java.Log;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Log
public class SaveSupervisors implements Action<OrderState, OrderEvent> {

    @Override
    public void execute(StateContext<OrderState, OrderEvent> context) {

        String leader = randomSupervisor(leaders());
        context.getExtendedState().getVariables().put(VacationOrder.LEADER_FIELD, leader);
        String manager = randomSupervisor(manager());
        context.getExtendedState().getVariables().put(VacationOrder.MANAGER_FIELD, manager);

        log.info("\u001B[35m" + "Save supervisors: LEADER - " + leader + ", MANAGER - " + manager + "\u001B[0m");
    }

    private String randomSupervisor(List<String> supervisors) {
        return supervisors.get(new Random().nextInt(supervisors.size()));
    }

    private List<String> leaders() {
        return Arrays.asList("Leader Maciek", "Leader Krzysiek");
    }

    private List<String> manager() {
        return Arrays.asList("Manager Zbyszek", "Manager Tadek");
    }
}
