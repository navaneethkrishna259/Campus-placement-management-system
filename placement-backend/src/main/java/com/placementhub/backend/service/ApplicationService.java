package com.placementhub.backend.service;

import com.placementhub.backend.dto.InterviewScheduleRequest;
import com.placementhub.backend.model.Application;
import java.util.List;

/**
 * OOP CONCEPT: ABSTRACTION
 * Interface defining the business logic contract for application and interview workflows.
 */
public interface ApplicationService {
    Application applyForJob(Long studentId, Long jobId);
    List<Application> getStudentApplications(Long studentId);
    List<Application> getCompanyApplications(Long companyId);
    Application getApplicationById(Long applicationId, Long requestingUserId, String role);
    void updateApplicationStatus(Long companyId, Long applicationId, String status);
    void scheduleInterview(Long companyId, Long applicationId, InterviewScheduleRequest request);
}
