package com.nyl.dependent.controller;

import com.nyl.dependent.model.Dependent;
import com.nyl.dependent.service.DependentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/V1")
public class DependentController {
    final static String SUCCESS = "Successfully ";
    final static String REMOVEBYID  = " remove dependent by ID ";
    final static String REMOVEBYGROUPID  = " remove dependent by Group ID ";

    Logger logger= LoggerFactory.getLogger(com.nyl.enrollee.controller.EnrolleeController.class);

    @Autowired
    private DependentService service;

    @PostMapping("/dependent")
    public Dependent createDependent(@Valid @RequestBody Dependent dependent) {
        return service.setDependent(dependent);
    }

    @GetMapping("/dependents")
    public List<Dependent> getAllDependents() {
        return service.getAllDependents();
    }

    @GetMapping("/dependent/{id}")
    public Dependent getDependentById(@PathVariable(value="id") int id) {
        return service.getDependentById(id);
    }

    @GetMapping("/dependentsByGroupID/{gid}")
    public List<Dependent> getDependentByGroupId(@PathVariable(value="gid") int groupId) {
        return service.getAllDependentsByGroupId(groupId);
    }

    @PutMapping("/dependent/{id}")
    public Dependent updateDependentById(@PathVariable(value="id") int id, @Valid @RequestBody Dependent dependent) {
        return service.updateDependentById(id, dependent);
    }

    @DeleteMapping("/dependent/{id}")
    public String deleteDependent(@PathVariable(value = "id") int id) {
        String logStr = SUCCESS + REMOVEBYID + id;

        // should handle the failure case in the future
        service.deleteDependentById(id);
        logger.info(logStr);

        return logStr;
    }

    /*
    @DeleteMapping("dependentsByGroupID/{gid}")
    public String deleteDependentByGroupId(@PathVariable(value = "gid") int groupId) {
        String logStr = SUCCESS + REMOVEBYGROUPID + groupId;

        // should handle the failure case in the future
        service.deleteDependentByGroupId(groupId);
        logger.info(logStr);

        return logStr;
    } */
}