package com.placementhub.backend.repository;

import com.placementhub.backend.model.Job;
import java.util.List;
import java.util.Optional;

/**
 * OOP CONCEPT: ABSTRACTION
 * Interface declaring data access operations for Job entity.
 */
public interface JobRepository {
    Long save(Job job);
    Optional<Job> findById(Long jobId);
    List<Job> findAllActive();
    List<Job> findByCompanyId(Long companyId);
    void updateActiveStatus(Long jobId, boolean isActive);
}
