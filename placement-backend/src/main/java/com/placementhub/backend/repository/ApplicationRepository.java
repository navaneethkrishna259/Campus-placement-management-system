package com.placementhub.backend.repository;

import com.placementhub.backend.model.Application;
import java.util.List;
import java.util.Optional;

/**
 * OOP CONCEPT: ABSTRACTION
 * Interface declaring data access operations for Application entity.
 */
public interface ApplicationRepository {
    Long save(Application application);
    Optional<Application> findById(Long applicationId);
    Optional<Application> findByStudentAndJob(Long studentId, Long jobId);
    List<Application> findByStudentId(Long studentId);
    List<Application> findByCompanyId(Long companyId);
    void updateStatus(Long applicationId, String status);
    void updateInterview(Long applicationId, String date, String time, String mode, String locationOrLink, String feedback);
}
