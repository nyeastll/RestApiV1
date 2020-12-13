package com.nyl.enrollee.repository;

import com.nyl.enrollee.model.Enrollee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrolleeRepository extends JpaRepository<Enrollee, Integer> {

}
