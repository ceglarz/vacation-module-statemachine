package com.ceglarski.vacation.order;

import com.ceglarski.vacation.order.event.OrderEvent;
import com.ceglarski.vacation.order.state.OrderState;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
class Service {
    private Repository repository;
    private StateMachine<OrderState, OrderEvent> stateMachine;
    private ObjectMapper objectMapper;

    Service(Repository repository) {
        createService(repository);
    }

    VacationOrder read(Integer vacationOrderId) {
        return repository.get(vacationOrderId);
    }

    VacationOrder create(VacationOrder vacationOrder) {
        vacationOrder.setStatus(OrderState.CREATED.name());
        VacationOrder createdVacationOrder = repository.create(vacationOrder);
        return this.changeState(createdVacationOrder.getId(), OrderEvent.START.name(), null);
    }

    VacationOrder changeState(Integer vacationOrderId, String event, HashMap<String, Object> data) {
        VacationOrder vacationOrder = repository.get(vacationOrderId);

        StateMachine<OrderState, OrderEvent> stateMachine = rehydrateStateMachine(vacationOrder);
        stateMachine.start();
        saveVariables(stateMachine, vacationOrder);
        stateMachine.sendEvent(prepareMessage(event, data));
        vacationOrder = updateVacationOrder(vacationOrderId, stateMachine);
        stateMachine.stop();
        return vacationOrder;
    }

    private void saveVariables(StateMachine<OrderState, OrderEvent> stateMachine, VacationOrder vacationOrder) {
        Map<String, Object> vacationOrderMap = objectMapper.convertValue(vacationOrder, Map.class);
        stateMachine.getExtendedState().getVariables().putAll(vacationOrderMap);
    }

    private Message<OrderEvent> prepareMessage(String event, HashMap<String, Object> data) {
        return MessageBuilder
                .withPayload(OrderEvent.valueOf(event))
                .setHeader("data", data)
                .build();
    }

    private StateMachine<OrderState, OrderEvent> rehydrateStateMachine(VacationOrder vacationOrder) {
        this.stateMachine.getStateMachineAccessor().doWithAllRegions(sma ->
                sma.resetStateMachine(new DefaultStateMachineContext<>(OrderState.valueOf(vacationOrder.getStatus()), null, null, this.stateMachine.getExtendedState()))
        );
        return this.stateMachine;
    }

    private VacationOrder updateVacationOrder(Integer vacationOrderId, StateMachine<OrderState, OrderEvent> stateMachine) {
        Map<Object, Object> variables = stateMachine.getExtendedState().getVariables();
        VacationOrder vacationOrder = VacationOrder.builder()
                .id(vacationOrderId)
                .status(stateMachine.getState().getId().name())
                .leader(String.valueOf(variables.getOrDefault(VacationOrder.LEADER_FIELD, null)))
                .manager(String.valueOf(variables.getOrDefault(VacationOrder.MANAGER_FIELD, null)))
                .build();
        return repository.update(vacationOrder);
    }

    private void createService(Repository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        VacationStateMachineBuilder vacationStateMachineBuilder = new VacationStateMachineBuilder();
        try {
            this.stateMachine = vacationStateMachineBuilder.buildMachine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}