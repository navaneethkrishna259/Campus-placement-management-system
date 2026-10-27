package com.placementhub.backend.controller;

import com.placementhub.backend.dto.ApiResponse;
import com.placementhub.backend.dto.LoginRequest;
import com.placementhub.backend.dto.LoginResponse;
import com.placementhub.backend.dto.StudentRegisterRequest;
import com.placementhub.backend.exception.AppException;
import com.placementhub.backend.model.Student;
import com.placementhub.backend.service.SessionManager;
import com.placementhub.backend.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Student>> register(@RequestBody StudentRegisterRequest request) {
        Student student = studentService.register(request);
        return ResponseEntity.ok(ApiResponse.ok("Student registered successfully", student));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse response = studentService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Student>> getProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        SessionManager.SessionData session = SessionManager.getSession(authHeader);
        if (session == null || !"STUDENT".equals(session.getRole())) {
            throw new AppException("Unauthorized. Please log in as a student.");
        }
        Student profile = studentService.getProfile(session.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(profile));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<Student>> updateProfile(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                              @RequestBody Student updatedData) {
        SessionManager.SessionData session = SessionManager.getSession(authHeader);
        if (session == null || !"STUDENT".equals(session.getRole())) {
            throw new AppException("Unauthorized. Please log in as a student.");
        }
        Student profile = studentService.updateProfile(session.getUserId(), updatedData);
        return ResponseEntity.ok(ApiResponse.ok("Profile updated successfully", profile));
    }
}
