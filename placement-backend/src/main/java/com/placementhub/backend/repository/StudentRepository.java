package com.placementhub.backend.repository;

import com.placementhub.backend.model.Student;
import java.util.Optional;

/**
 * OOP CONCEPT: ABSTRACTION
 * Interface decoupling data access logic from service implementation.
 */
public interface StudentRepository {
    Long save(Student student);
    Optional<Student> findByEmail(String email);
    Optional<Student> findById(Long id);
    void update(Student student);
}
