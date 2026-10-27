package com.placementhub.backend.service;

import com.placementhub.backend.dto.JobPostRequest;
import com.placementhub.backend.model.Job;
import java.util.List;

/**
 * OOP CONCEPT: ABSTRACTION
 * Interface defining the business logic contract for job operations.
 */
public interface JobService {
    Job postJob(Long companyId, JobPostRequest request);
    List<Job> getActiveJobs(Long studentId); // studentId can be null, if provided sets isSaved
    List<Job> getCompanyJobs(Long companyId);
    Job getJobById(Long jobId);
    void cancelJob(Long companyId, Long jobId);
}
