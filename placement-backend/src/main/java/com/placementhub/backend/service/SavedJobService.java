package com.placementhub.backend.service;

import com.placementhub.backend.model.SavedJob;
import java.util.List;

/**
 * OOP CONCEPT: ABSTRACTION
 * Interface defining contract for managing student saved jobs.
 */
public interface SavedJobService {
    void saveJob(Long studentId, Long jobId);
    void removeSavedJob(Long studentId, Long jobId);
    List<SavedJob> getSavedJobs(Long studentId);
}
