package com.ceglarski.vacation.order;

import com.ceglarski.vacation.order.event.OrderEvent;
import com.ceglarski.vacation.order.state.OrderState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceTest {

    private Repository repository;
    private Service service;

    @BeforeEach
    void setUp() {
        repository = new Repository();
        service = new Service(repository);
    }

    @Test
    void shouldDateNotAvailableWhenDateNotAvailable() {

        VacationOrder vacationOrder = VacationOrder.builder()
                .employee("Oskar").status(OrderState.CREATED.name()).name("shouldDateNotAvailableWhenDateNotAvailable").dateAvailable(false)
                .build();

        //when
        VacationOrder result = service.create(vacationOrder);

        //then
        assertEquals(OrderState.DATE_NOT_AVAILABLE.name(), result.getStatus());
        System.out.println(result.toString());
    }

    @Test
    void shouldAskLeaderWhenDateAvailable() {

        VacationOrder vacationOrder = VacationOrder.builder()
                .employee("Oskar").status(OrderState.CREATED.name()).name("shouldAskLeaderWhenDateAvailable").dateAvailable(true)
                .build();

        //when
        VacationOrder result = service.create(vacationOrder);

        //then
        assertEquals(OrderState.LEADER_WAS_ASKED.name(), result.getStatus());
        System.out.println(result.toString());
    }

    @Test
    void shouldAskManagerWhenLeaderAccepted() {

        VacationOrder vacationOrder = VacationOrder.builder()
                .employee("Oskar").status(OrderState.LEADER_WAS_ASKED.name()).name("shouldAskManagerWhenLeaderAccepted").dateAvailable(true)
                .leader("Leader Testowy").manager("Manager Testowy")
                .build();
        vacationOrder = repository.create(vacationOrder);

        HashMap<String, Object> data = new HashMap<>();
        data.put("response", true);

        //when
        VacationOrder result = service.changeState(vacationOrder.getId(), OrderEvent.LEADER_RESPONDED.name(), data);

        //then
        assertEquals(OrderState.MANAGER_WAS_ASKED.name(), result.getStatus());
        System.out.println(result.toString());
    }

    @Test
    void shouldAcceptedWhenManagerAccepted() {

        VacationOrder vacationOrder = VacationOrder.builder()
                .employee("Oskar").status(OrderState.MANAGER_WAS_ASKED.name()).name("shouldAskManagerWhenLeaderAccepted").dateAvailable(true)
                .leader("Leader Testowy").manager("Manager Testowy")
                .build();
        vacationOrder = repository.create(vacationOrder);

        //when
        VacationOrder result = service.changeState(vacationOrder.getId(), OrderEvent.MANAGER_RESPONDED.name(), null);

        //then
        assertEquals(OrderState.ACCEPTED.name(), result.getStatus());
        System.out.println(result.toString());
    }
}