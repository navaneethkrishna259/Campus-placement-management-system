package com.placementhub.backend.controller;

import com.placementhub.backend.dto.ApiResponse;
import com.placementhub.backend.dto.InterviewScheduleRequest;
import com.placementhub.backend.dto.StatusUpdateRequest;
import com.placementhub.backend.exception.AppException;
import com.placementhub.backend.model.Application;
import com.placementhub.backend.service.ApplicationService;
import com.placementhub.backend.service.SessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // POST /api/jobs/{jobId}/applications
    @PostMapping("/api/jobs/{jobId}/applications")
    public ResponseEntity<ApiResponse<Application>> applyForJob(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                                 @PathVariable Long jobId) {
        SessionManager.SessionData session = SessionManager.getSession(authHeader);
        if (session == null || !"STUDENT".equals(session.getRole())) {
            throw new AppException("Unauthorized. Please log in as a student.");
        }
        Application app = applicationService.applyForJob(session.getUserId(), jobId);
        return ResponseEntity.ok(ApiResponse.ok("Applied successfully", app));
    }

    // GET /api/students/applications
    @GetMapping("/api/students/applications")
    public ResponseEntity<ApiResponse<List<Application>>> getStudentApplications(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        SessionManager.SessionData session = SessionManager.getSession(authHeader);
        if (session == null || !"STUDENT".equals(session.getRole())) {
            throw new AppException("Unauthorized. Please log in as a student.");
        }
        List<Application> apps = applicationService.getStudentApplications(session.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(apps));
    }

    // GET /api/students/interviews
    @GetMapping("/api/students/interviews")
    public ResponseEntity<ApiResponse<List<Application>>> getStudentInterviews(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        SessionManager.SessionData session = SessionManager.getSession(authHeader);
        if (session == null || !"STUDENT".equals(session.getRole())) {
            throw new AppException("Unauthorized. Please log in as a student.");
        }
        // Scheduled interviews have status = 'INTERVIEW_SCHEDULED'
        List<Application> apps = applicationService.getStudentApplications(session.getUserId()).stream()
                .filter(a -> "INTERVIEW_SCHEDULED".equalsIgnoreCase(a.getStatus()) && a.getInterviewDate() != null)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(apps));
    }

    // GET /api/companies/applications
    @GetMapping("/api/companies/applications")
    public ResponseEntity<ApiResponse<List<Application>>> getCompanyApplications(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        SessionManager.SessionData session = SessionManager.getSession(authHeader);
        if (session == null || !"COMPANY".equals(session.getRole())) {
            throw new AppException("Unauthorized. Please log in as a company.");
        }
        List<Application> apps = applicationService.getCompanyApplications(session.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(apps));
    }

    // GET /api/applications/{applicationId}
    @GetMapping("/api/applications/{applicationId}")
    public ResponseEntity<ApiResponse<Application>> getApplicationById(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                                        @PathVariable Long applicationId) {
        SessionManager.SessionData session = SessionManager.getSession(authHeader);
        if (session == null) {
            throw new AppException("Unauthorized.");
        }
        Application app = applicationService.getApplicationById(applicationId, session.getUserId(), session.getRole());
        return ResponseEntity.ok(ApiResponse.ok(app));
    }

    // PATCH /api/companies/applications/{applicationId}/status
    @PatchMapping("/api/companies/applications/{applicationId}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                          @PathVariable Long applicationId,
                                                          @RequestBody StatusUpdateRequest request) {
        SessionManager.SessionData session = SessionManager.getSession(authHeader);
        if (session == null || !"COMPANY".equals(session.getRole())) {
            throw new AppException("Unauthorized. Please log in as a company.");
        }
        if (request.getStatus() == null || request.getStatus().trim().isEmpty()) {
            throw new AppException("Status is required");
        }
        applicationService.updateApplicationStatus(session.getUserId(), applicationId, request.getStatus().trim());
        return ResponseEntity.ok(ApiResponse.ok("Application status updated to " + request.getStatus().trim(), null));
    }

    // POST /api/companies/applications/{applicationId}/interview
    @PostMapping("/api/companies/applications/{applicationId}/interview")
    public ResponseEntity<ApiResponse<Void>> scheduleInterview(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                               @PathVariable Long applicationId,
                                                               @RequestBody InterviewScheduleRequest request) {
        SessionManager.SessionData session = SessionManager.getSession(authHeader);
        if (session == null || !"COMPANY".equals(session.getRole())) {
            throw new AppException("Unauthorized. Please log in as a company.");
        }
        applicationService.scheduleInterview(session.getUserId(), applicationId, request);
        return ResponseEntity.ok(ApiResponse.ok("Interview scheduled successfully", null));
    }
}
