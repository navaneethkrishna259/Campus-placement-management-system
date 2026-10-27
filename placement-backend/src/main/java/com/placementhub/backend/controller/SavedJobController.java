package com.placementhub.backend.controller;

import com.placementhub.backend.dto.ApiResponse;
import com.placementhub.backend.exception.AppException;
import com.placementhub.backend.model.SavedJob;
import com.placementhub.backend.service.SavedJobService;
import com.placementhub.backend.service.SessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students/saved-jobs")
public class SavedJobController {

    private final SavedJobService savedJobService;

    public SavedJobController(SavedJobService savedJobService) {
        this.savedJobService = savedJobService;
    }

    // GET /api/students/saved-jobs
    @GetMapping
    public ResponseEntity<ApiResponse<List<SavedJob>>> getSavedJobs(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        SessionManager.SessionData session = SessionManager.getSession(authHeader);
        if (session == null || !"STUDENT".equals(session.getRole())) {
            throw new AppException("Unauthorized. Please log in as a student.");
        }
        List<SavedJob> savedJobs = savedJobService.getSavedJobs(session.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(savedJobs));
    }

    // POST /api/students/saved-jobs/{jobId}
    @PostMapping("/{jobId}")
    public ResponseEntity<ApiResponse<Void>> saveJob(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                     @PathVariable Long jobId) {
        SessionManager.SessionData session = SessionManager.getSession(authHeader);
        if (session == null || !"STUDENT".equals(session.getRole())) {
            throw new AppException("Unauthorized. Please log in as a student.");
        }
        savedJobService.saveJob(session.getUserId(), jobId);
        return ResponseEntity.ok(ApiResponse.ok("Job saved successfully", null));
    }

    // DELETE /api/students/saved-jobs/{jobId}
    @DeleteMapping("/{jobId}")
    public ResponseEntity<ApiResponse<Void>> removeSavedJob(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                            @PathVariable Long jobId) {
        SessionManager.SessionData session = SessionManager.getSession(authHeader);
        if (session == null || !"STUDENT".equals(session.getRole())) {
            throw new AppException("Unauthorized. Please log in as a student.");
        }
        savedJobService.removeSavedJob(session.getUserId(), jobId);
        return ResponseEntity.ok(ApiResponse.ok("Saved job removed successfully", null));
    }
}
