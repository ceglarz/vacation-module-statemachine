package com.ceglarski.vacation.order;

import com.ceglarski.vacation.order.state.OrderState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
class Repository {

    private Map<Integer, VacationOrder> vacationOrderMap = new HashMap<>();

    VacationOrder create(VacationOrder vacationOrder) {
        vacationOrder.setId(vacationOrderMap.size() + 1);
        vacationOrderMap.put(vacationOrder.getId(), vacationOrder);
        return vacationOrder;
    }

    VacationOrder get(Integer vacationOrderId) {
        return vacationOrderMap.get(vacationOrderId);
    }

    VacationOrder update(VacationOrder newVacationOrder) {
        VacationOrder vacationOrder = get(newVacationOrder.getId());
        vacationOrder.setStatus(newVacationOrder.getStatus());
        if (isNotBlank(newVacationOrder.getLeader())) {
            vacationOrder.setLeader(newVacationOrder.getLeader());
        }
        if (isNotBlank(newVacationOrder.getManager())) {
            vacationOrder.setManager(newVacationOrder.getManager());
        }
        vacationOrder.setId(vacationOrder.getId());
        vacationOrder.setEmployee(vacationOrder.getEmployee());
        vacationOrder.setDateAvailable(vacationOrder.isDateAvailable());
        vacationOrder.setName(vacationOrder.getName());
        vacationOrderMap.put(vacationOrder.getId(), vacationOrder);
        return vacationOrder;
    }
}
