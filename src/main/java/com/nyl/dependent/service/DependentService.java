package com.nyl.dependent.service;

import com.nyl.dependent.model.Dependent;
import com.nyl.dependent.repository.DependentRepository;
import com.nyl.enrollee.exception.ResourceNotAvailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DependentService {
    Logger logger = LoggerFactory.getLogger(DependentService.class);

    @Autowired
    private DependentRepository repository;

    public Dependent setDependent(Dependent dependent) {
        return repository.save(dependent);
    }

    public List<Dependent> getAllDependents() {
        return repository.findAll();
    }

    public List<Dependent> getAllDependentsByGroupId(int groupId) {
        List<Dependent> dependentList = new ArrayList<Dependent>();

        repository.findAll().stream()
                .filter((t) -> t.getGroupid() == groupId)
                .forEach(dependentList::add);

        return dependentList;
    }

    public Dependent getDependentById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotAvailableException("Dependent", "id", Integer.toString(id)));
    }

    public Dependent updateDependentById(int id, @Valid Dependent newDependent) {
        Dependent dependent = repository.findById(id)
                .orElseThrow(() -> new ResourceNotAvailableException("Dependent", "id", Integer.toString(id)));

        dependent.setName(newDependent.getName());
        dependent.setDob(newDependent.getDob());

        repository.save(dependent);
        return dependent;
    }

    public ResponseEntity<String> deleteDependentById(int id) {
        Dependent dependent = repository.findById(id)
                .orElseThrow(() -> new ResourceNotAvailableException("Dependent", "id", Integer.toString(id)));

        repository.delete(dependent);

        return ResponseEntity.ok().build();
    }

    /*
    public ResponseEntity<String> deleteDependentByGroupId(int groupId) {
        repository.findAll().stream()
                .filter((t) -> t.getGroupid() == groupId)
                .forEach((dependent) -> repository.delete(dependent));

        return ResponseEntity.ok().build();
    } */
}
