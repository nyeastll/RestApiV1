package com.nyl.enrollee.service;

import com.nyl.dependent.model.Dependent;
import com.nyl.enrollee.repository.EnrolleeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class EnrollDependentService {
    final static String ONEDEPENDENT_POSTURI = "http://localhost:8080/api/V1/dependent";
    final static String DEPENDENTS_BYGROUPID_GETURI = "http://localhost:8080/api/V1/dependentsByGroupID/{gid}";
    final static String DELETEDEPENDENT_POSTURI = "http://localhost:8080/api/V1/dependent/{id}";

    Logger logger = LoggerFactory.getLogger(EnrolleeService.class);

    @Autowired
    private RestTemplate template;

    @Autowired
    private EnrolleeRepository repository;

    public  List<Dependent> getDependents(int groupId) {
        List<Dependent> dependentList = new ArrayList<Dependent>();

        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("gid", groupId);
        ResponseEntity<Dependent[]>  responseEntity = template.getForEntity(
                DEPENDENTS_BYGROUPID_GETURI,
                Dependent[].class,
                params);

        dependentList = Arrays.asList(responseEntity.getBody().clone());

        return dependentList;
    }

    public ResponseEntity<String> setDependents(int groupId, List<Dependent> dependentList) {
        String responseStr = "";

        for(Dependent dependent : dependentList) {
            logger.info("Send Dependent Rest Call Request with Dependent Id " + dependent.getId());

            dependent.setGroupid(groupId);
            Dependent dependentResponse = template.postForObject(
                    ONEDEPENDENT_POSTURI,
                    dependent,
                    Dependent.class);
            responseStr = dependentResponse.getGroupid()  == groupId ?
                    "Successfully add dependent for Enroll with groupId" + groupId
                    :
                    "Failed add dependent for Enroll with groupId" + groupId;
            logger.info(responseStr);
        }
        return ResponseEntity.ok().build();
    }

    public void deleteDependents(int dependentgroupid) {
        String responseStr = "";

        List<Dependent> dependentList = new ArrayList<Dependent>();
        dependentList = getDependents(dependentgroupid);

        for(Dependent dependent : dependentList) {
            logger.info("Delete Dependent Rest Call Request with Dependent Id " + dependent.getId());

            Map<String, Integer> params = new HashMap<String, Integer>();
            params.put("id", dependent.getId());
            template.delete(DELETEDEPENDENT_POSTURI, params);
            responseStr = "Successfully delete dependent with Id " + dependent.getId();
            logger.info(responseStr);
        }
    }
}
