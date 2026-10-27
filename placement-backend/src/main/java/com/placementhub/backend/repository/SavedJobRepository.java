package com.placementhub.backend.repository;

import com.placementhub.backend.model.SavedJob;
import java.util.List;
import java.util.Optional;

/**
 * OOP CONCEPT: ABSTRACTION
 * Interface declaring data access operations for SavedJob entity.
 */
public interface SavedJobRepository {
    Long save(Long studentId, Long jobId);
    void delete(Long studentId, Long jobId);
    Optional<SavedJob> findByStudentAndJob(Long studentId, Long jobId);
    List<SavedJob> findByStudentId(Long studentId);
}
