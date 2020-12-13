package com.nyl.enrollee.service;

import com.nyl.dependent.model.Dependent;
import com.nyl.enrollee.exception.ResourceNotAvailableException;
import com.nyl.enrollee.model.Enrollee;
import com.nyl.enrollee.repository.EnrolleeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EnrolleeService {
    final static String ONEDEPENDENTPOST_URI = "http://localhost:8080/api/V1/dependent";

    Logger logger = LoggerFactory.getLogger(EnrolleeService.class);

    @Autowired
    private RestTemplate template;

    @Autowired
    private EnrolleeRepository repository;

    public Enrollee setEnrollee(Enrollee enrollee) {
        return repository.save(enrollee);
    }

    public List<Enrollee> getAllEnrollee() {
        return repository.findAll();
    }

    public Enrollee getEnrolleeById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotAvailableException("Enrollee", "id", Integer.toString(id)));
    }

    public Enrollee updateEnrolleeById(int id, Enrollee newEnrollee) {
        Enrollee enrollee = repository.findById(id)
                .orElseThrow(() -> new ResourceNotAvailableException("Enrollee", "id", Integer.toString(id)));

        enrollee.setName(newEnrollee.getName());
        enrollee.setActivationStatus(newEnrollee.getActivationStatus());
        enrollee.setDob(newEnrollee.getDob());
        if (newEnrollee.getPhoneNum() != null)
            enrollee.setPhoneNum(newEnrollee.getPhoneNum());

        repository.save(enrollee);
        return enrollee;
    }

    public ResponseEntity<String> deleteEnrolleeById(int id) {
        Enrollee enrollee = repository.findById(id)
                .orElseThrow(() -> new ResourceNotAvailableException("Enrollee", "id", Integer.toString(id)));

        repository.delete(enrollee);

        return ResponseEntity.ok().build();
    }
}
