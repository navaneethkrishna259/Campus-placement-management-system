package com.placementhub.backend.controller;

import com.placementhub.backend.dto.ApiResponse;
import com.placementhub.backend.dto.CompanyRegisterRequest;
import com.placementhub.backend.dto.LoginRequest;
import com.placementhub.backend.dto.LoginResponse;
import com.placementhub.backend.exception.AppException;
import com.placementhub.backend.model.Company;
import com.placementhub.backend.service.CompanyService;
import com.placementhub.backend.service.SessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Company>> register(@RequestBody CompanyRegisterRequest request) {
        Company company = companyService.register(request);
        return ResponseEntity.ok(ApiResponse.ok("Company registered successfully", company));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse response = companyService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Company>> getProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        SessionManager.SessionData session = SessionManager.getSession(authHeader);
        if (session == null || !"COMPANY".equals(session.getRole())) {
            throw new AppException("Unauthorized. Please log in as a company.");
        }
        Company profile = companyService.getProfile(session.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(profile));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<Company>> updateProfile(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                              @RequestBody Company updatedData) {
        SessionManager.SessionData session = SessionManager.getSession(authHeader);
        if (session == null || !"COMPANY".equals(session.getRole())) {
            throw new AppException("Unauthorized. Please log in as a company.");
        }
        Company profile = companyService.updateProfile(session.getUserId(), updatedData);
        return ResponseEntity.ok(ApiResponse.ok("Profile updated successfully", profile));
    }
}
