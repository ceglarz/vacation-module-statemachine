package com.ceglarski.vacation.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final Service service;

    @GetMapping("api/vacation/{id}")
    public VacationOrder read(@PathVariable("id") Integer id) {
        return service.read(id);
    }

    @PostMapping("api/vacation")
    public VacationOrder create(@RequestBody VacationOrder vacationOrder) {
        return service.create(vacationOrder);
    }

    @PostMapping("api/vacation/{id}/{event}")
    public VacationOrder changeState(@PathVariable("id") Integer id, @PathVariable("event") String event, @RequestBody HashMap<String, Object> vacationOrder) {
        return service.changeState(id, event, vacationOrder);
    }
}
