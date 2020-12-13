package com.nyl.enrollee.controller;

import com.nyl.dependent.model.Dependent;
import com.nyl.enrollee.model.Enrollee;
import com.nyl.enrollee.service.EnrolleeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/V1")
public class EnrolleeController {
    final static String SUCCESS = "Successfully ";
    final static String REMOVEBYID  = " remove enrollee by ID ";

    Logger logger= LoggerFactory.getLogger(EnrolleeController.class);

    @Autowired
    private EnrolleeService service;

    @PostMapping("/enrollees")
    public Enrollee createEnrollee(@Valid @RequestBody Enrollee enrollee) {
        return service.setEnrollee(enrollee);
    }

    @GetMapping("/enrollees")
    public List<Enrollee> getAllEnrollee() {
        return service.getAllEnrollee();
    }

    @GetMapping("/enrollee/{id}")
    public Enrollee getEnrolleeById(@PathVariable(value="id") int id) {
        return service.getEnrolleeById(id);
    }

    @PutMapping("/enrollee/{id}")
    public Enrollee updateEnrolleeById(@PathVariable(value="id") int id, @Valid @RequestBody Enrollee enrollee) {
        return service.updateEnrolleeById(id, enrollee);
    }

    @DeleteMapping("/enrollee/{id}")
    public String deleteEnrollee(@PathVariable(value = "id") int id) {
        String logStr = SUCCESS + REMOVEBYID + id;

        // should handle the failure case in the future
        service.deleteEnrolleeById(id);
        logger.info(logStr);

        return logStr;
    }
}
