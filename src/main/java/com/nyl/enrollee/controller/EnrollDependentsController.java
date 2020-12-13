package com.nyl.enrollee.controller;

import com.nyl.dependent.model.Dependent;
import com.nyl.enrollee.model.Enrollee;
import com.nyl.enrollee.service.EnrollDependentService;
import com.nyl.enrollee.service.EnrolleeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/V1")
public class EnrollDependentsController {
    final static String SUCCESS = "Successfully ";
    final static String FAILED  = "Failed to ";
    final static String REMOVEBYGROUPID  = " remove enrollee's dependents by group ID ";

    Logger logger= LoggerFactory.getLogger(EnrolleeController.class);

    @Autowired
    private EnrolleeService enrolleeService;

    @Autowired
    private EnrollDependentService enrollDependentServiceService;

    @GetMapping("/enrollee/{eid}/dependents")
    public List<Dependent> getDependents(@PathVariable(value = "eid") int enrolleeId) {
        // find the enrollee from Enroll table by id
        Enrollee enrollee = enrolleeService.getEnrolleeById(enrolleeId);

        // get dependentgroupid from Enroll table, and make sure the enrollee is active
        if (enrollee == null || !enrollee.getActivationStatus()) {
            // return some hint, ask to add enrollee
            logger.warn("Enrollee with Id " + enrolleeId + " does not exist, please add Enrollee first or make sure the Enrollee is active");
            return Collections.emptyList();
        } else {
            logger.info("Find Enrollee with Id " + enrolleeId + " group id " + enrollee.getDependentgroupid());
        }

        List<Dependent> dependentList = new ArrayList<Dependent>();
        int groupId = enrolleeService.getEnrolleeById(enrolleeId).getDependentgroupid();
        logger.info("retrieve all the Dependents for Enrollee with groupId " + groupId);

        return enrollDependentServiceService.getDependents(groupId);
    }

    @PostMapping("/enrollee/{eid}/dependents")
    public List<Dependent> addDependents(@PathVariable(value="eid") int enrolleeId,
                                         @RequestBody List<Dependent> dependentList) {
        // find the enrollee from Enroll table by id
        Enrollee enrollee = enrolleeService.getEnrolleeById(enrolleeId);

        // get dependentgroupid from Enroll table, and make sure the enrollee is active
        if (enrollee == null || !enrollee.getActivationStatus()) {
            // return some hint, ask to add enrollee
            logger.warn("Enrollee with Id " + enrolleeId + " does not exist, please add Enrollee first or make sure the Enrollee is active");
            return Collections.emptyList();
        } else {
            logger.info("Find Enrollee with Id " + enrolleeId + " group id " + enrollee.getDependentgroupid());
        }

        // rest call here to add list of dependents into Dependent table with groupId
        enrollDependentServiceService.setDependents(enrollee.getDependentgroupid(), dependentList);
        return dependentList;
    }

    @DeleteMapping("/enrollee/{eid}/dependents")
    public String deleteEnrollee(@PathVariable(value = "eid") int enrolleeId) {
        String logStr = REMOVEBYGROUPID + enrolleeId;

        // find the enrollee from Enroll table by id
        Enrollee enrollee = enrolleeService.getEnrolleeById(enrolleeId);

        // get dependentgroupid from Enroll table, and make sure the enrollee is active
        if (enrollee == null || !enrollee.getActivationStatus()) {
            // return some hint, ask to add enrollee
            logger.warn("Enrollee with Id " + enrolleeId + " does not exist, please add Enrollee first or make sure the Enrollee is active");
            logStr = FAILED + logStr;
            return logStr;
        } else {
            logger.info("Find Enrollee with Id " + enrolleeId + " group id " + enrollee.getDependentgroupid());
        }

        // should handle the failure case in the future
        enrollDependentServiceService.deleteDependents(enrollee.getDependentgroupid());
        logStr = SUCCESS + logStr;
        logger.info(logStr);

        return logStr;
    }
}
