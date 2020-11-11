package com.ceglarski.vacation.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final Service service;

    @PostMapping("api/vacation")
    public VacationOrder create(@RequestBody VacationOrder vacationOrder) {
        return service.create(vacationOrder);
    }

    @PostMapping("api/vacation/{id}/{event}")
    public VacationOrder changeState(@PathVariable("id") Integer id, @PathVariable("event") String event, @RequestBody HashMap<String, Object> vacationOrder) {
        return service.changeState(id, event, vacationOrder);
    }
}
