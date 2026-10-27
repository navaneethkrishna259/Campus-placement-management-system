package com.placementhub.backend.service;

import com.placementhub.backend.dto.InterviewScheduleRequest;
import com.placementhub.backend.exception.AppException;
import com.placementhub.backend.model.Application;
import com.placementhub.backend.model.Job;
import com.placementhub.backend.repository.ApplicationRepository;
import com.placementhub.backend.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    public Application applyForJob(Long studentId, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new AppException("Job not found"));

        if (Boolean.FALSE.equals(job.getIsActive())) {
            throw new AppException("This job is no longer active and cannot accept new applications.");
        }

        if (applicationRepository.findByStudentAndJob(studentId, jobId).isPresent()) {
            throw new AppException("You have already applied for this job.");
        }

        Application application = new Application();
        application.setStudentId(studentId);
        application.setJobId(jobId);
        application.setStatus("APPLIED");

        Long id = applicationRepository.save(application);
        application.setApplicationId(id);
        return application;
    }

    @Override
    public List<Application> getStudentApplications(Long studentId) {
        return applicationRepository.findByStudentId(studentId);
    }

    @Override
    public List<Application> getCompanyApplications(Long companyId) {
        return applicationRepository.findByCompanyId(companyId);
    }

    @Override
    public Application getApplicationById(Long applicationId, Long requestingUserId, String role) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException("Application not found"));

        if ("STUDENT".equals(role) && !app.getStudentId().equals(requestingUserId)) {
            throw new AppException("Unauthorized to access this application");
        }
        if ("COMPANY".equals(role)) {
            Job job = jobRepository.findById(app.getJobId())
                    .orElseThrow(() -> new AppException("Job not found"));
            if (!job.getCompanyId().equals(requestingUserId)) {
                throw new AppException("Unauthorized to access this application");
            }
        }

        return app;
    }

    @Override
    public void updateApplicationStatus(Long companyId, Long applicationId, String status) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException("Application not found"));

        Job job = jobRepository.findById(app.getJobId())
                .orElseThrow(() -> new AppException("Job not found"));

        if (!job.getCompanyId().equals(companyId)) {
            throw new AppException("Unauthorized: Only the company that posted this job can update application status");
        }

        applicationRepository.updateStatus(applicationId, status.toUpperCase());
    }

    @Override
    public void scheduleInterview(Long companyId, Long applicationId, InterviewScheduleRequest request) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException("Application not found"));

        Job job = jobRepository.findById(app.getJobId())
                .orElseThrow(() -> new AppException("Job not found"));

        if (!job.getCompanyId().equals(companyId)) {
            throw new AppException("Unauthorized: Only the company that posted this job can schedule interviews");
        }

        if (request.getInterviewDate() == null || request.getInterviewDate().trim().isEmpty()) {
            throw new AppException("Interview date is required");
        }
        if (request.getInterviewTime() == null || request.getInterviewTime().trim().isEmpty()) {
            throw new AppException("Interview time is required");
        }
        if (request.getInterviewMode() == null || request.getInterviewMode().trim().isEmpty()) {
            throw new AppException("Interview mode (Online / Offline) is required");
        }

        applicationRepository.updateInterview(
                applicationId,
                request.getInterviewDate().trim(),
                request.getInterviewTime().trim(),
                request.getInterviewMode().trim(),
                request.getInterviewLocationOrLink() != null ? request.getInterviewLocationOrLink().trim() : "",
                request.getCompanyFeedback() != null ? request.getCompanyFeedback().trim() : ""
        );
    }
}
