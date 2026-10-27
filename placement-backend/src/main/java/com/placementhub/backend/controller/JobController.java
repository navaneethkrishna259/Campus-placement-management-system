package com.placementhub.backend.controller;

import com.placementhub.backend.dto.ApiResponse;
import com.placementhub.backend.dto.JobPostRequest;
import com.placementhub.backend.exception.AppException;
import com.placementhub.backend.model.Job;
import com.placementhub.backend.service.JobService;
import com.placementhub.backend.service.SessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // GET /api/jobs (all active jobs, optional student auth to flag isSaved)
    @GetMapping("/api/jobs")
    public ResponseEntity<ApiResponse<List<Job>>> getActiveJobs(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long studentId = null;
        if (authHeader != null) {
            SessionManager.SessionData session = SessionManager.getSession(authHeader);
            if (session != null && "STUDENT".equals(session.getRole())) {
                studentId = session.getUserId();
            }
        }
        List<Job> jobs = jobService.getActiveJobs(studentId);
        return ResponseEntity.ok(ApiResponse.ok(jobs));
    }

    // GET /api/jobs/{jobId}
    @GetMapping("/api/jobs/{jobId}")
    public ResponseEntity<ApiResponse<Job>> getJobById(@PathVariable Long jobId) {
        Job job = jobService.getJobById(jobId);
        return ResponseEntity.ok(ApiResponse.ok(job));
    }

    // POST /api/companies/jobs
    @PostMapping("/api/companies/jobs")
    public ResponseEntity<ApiResponse<Job>> postJob(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                    @RequestBody JobPostRequest request) {
        SessionManager.SessionData session = SessionManager.getSession(authHeader);
        if (session == null || !"COMPANY".equals(session.getRole())) {
            throw new AppException("Unauthorized. Please log in as a company.");
        }
        Job created = jobService.postJob(session.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Job posted successfully", created));
    }

    // GET /api/companies/jobs
    @GetMapping("/api/companies/jobs")
    public ResponseEntity<ApiResponse<List<Job>>> getCompanyJobs(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        SessionManager.SessionData session = SessionManager.getSession(authHeader);
        if (session == null || !"COMPANY".equals(session.getRole())) {
            throw new AppException("Unauthorized. Please log in as a company.");
        }
        List<Job> jobs = jobService.getCompanyJobs(session.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(jobs));
    }

    // PATCH /api/companies/jobs/{jobId}/cancel
    @PatchMapping("/api/companies/jobs/{jobId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelJob(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                       @PathVariable Long jobId) {
        SessionManager.SessionData session = SessionManager.getSession(authHeader);
        if (session == null || !"COMPANY".equals(session.getRole())) {
            throw new AppException("Unauthorized. Please log in as a company.");
        }
        jobService.cancelJob(session.getUserId(), jobId);
        return ResponseEntity.ok(ApiResponse.ok("Job cancelled successfully", null));
    }
}
