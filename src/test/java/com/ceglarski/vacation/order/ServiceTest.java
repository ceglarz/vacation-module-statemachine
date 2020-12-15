package com.ceglarski.vacation.order;

import com.ceglarski.vacation.order.event.OrderEvent;
import com.ceglarski.vacation.order.state.OrderState;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log
@TestMethodOrder(OrderAnnotation.class)
class ServiceTest {

    private Repository repository;
    private Service service;

    @BeforeEach
    void setUp() {
        repository = new Repository();
        service = new Service(repository);
    }

    @Test
    @Order(1)
    void shouldDateNotAvailableWhenDateNotAvailable() {

        VacationOrder vacationOrder = VacationOrder.builder()
                .employee("Oskar").status(OrderState.CREATED.name()).name("shouldDateNotAvailableWhenDateNotAvailable").dateAvailable(false)
                .build();

        //when
        VacationOrder result = service.create(vacationOrder);
        log(result.toString());

        //then
        assertEquals(OrderState.DATE_NOT_AVAILABLE.name(), result.getStatus());
    }

    @Test
    @Order(2)
    void shouldAskLeaderWhenDateAvailable() {

        VacationOrder vacationOrder = VacationOrder.builder()
                .employee("Oskar").status(OrderState.CREATED.name()).name("shouldAskLeaderWhenDateAvailable").dateAvailable(true)
                .build();

        //when
        VacationOrder result = service.create(vacationOrder);
        log(result.toString());

        //then
        assertEquals(OrderState.LEADER_WAS_ASKED.name(), result.getStatus());
    }

    @Test
    @Order(3)
    void shouldAskManagerWhenLeaderDidAccept() {

        VacationOrder vacationOrder = VacationOrder.builder()
                .employee("Oskar").status(OrderState.LEADER_WAS_ASKED.name()).name("shouldAskManagerWhenLeaderDidAccept").dateAvailable(true)
                .leader("Leader Testowy").manager("Manager Testowy")
                .build();
        vacationOrder = repository.create(vacationOrder);

        HashMap<String, Object> data = new HashMap<>();
        data.put("response", true);

        //when
        VacationOrder result = service.changeState(vacationOrder.getId(), OrderEvent.LEADER_RESPONDED.name(), data);
        log(result.toString());

        //then
        assertEquals(OrderState.MANAGER_WAS_ASKED.name(), result.getStatus());
    }

    @Test
    @Order(4)
    void shouldRejectedWhenLeaderDidNotAccept() {

        VacationOrder vacationOrder = VacationOrder.builder()
                .employee("Oskar").status(OrderState.LEADER_WAS_ASKED.name()).name("shouldRejectedWhenLeaderDidNotAccept").dateAvailable(true)
                .leader("Leader Testowy").manager("Manager Testowy")
                .build();
        vacationOrder = repository.create(vacationOrder);

        HashMap<String, Object> data = new HashMap<>();
        data.put("response", false);

        //when
        VacationOrder result = service.changeState(vacationOrder.getId(), OrderEvent.LEADER_RESPONDED.name(), data);
        log(result.toString());

        //then
        assertEquals(OrderState.REJECTED.name(), result.getStatus());
    }

    @Test
    @Order(5)
    void shouldAcceptedWhenManagerDidAccept() {

        VacationOrder vacationOrder = VacationOrder.builder()
                .employee("Oskar").status(OrderState.MANAGER_WAS_ASKED.name()).name("shouldAcceptedWhenManagerDidAccept").dateAvailable(true)
                .leader("Leader Testowy").manager("Manager Testowy")
                .build();
        vacationOrder = repository.create(vacationOrder);

        HashMap<String, Object> data = new HashMap<>();
        data.put("response", true);

        //when
        VacationOrder result = service.changeState(vacationOrder.getId(), OrderEvent.MANAGER_RESPONDED.name(), data);
        log(result.toString());

        //then
        assertEquals(OrderState.ACCEPTED.name(), result.getStatus());
    }

    private void log(String message) {
        log.info("\u001B[32m" + message + "\u001B[0m");
    }
}