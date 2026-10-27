package com.placementhub.backend.controller;

import com.placementhub.backend.dto.ApiResponse;
import com.placementhub.backend.service.SessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null) {
            SessionManager.invalidateSession(authHeader);
        }
        return ResponseEntity.ok(ApiResponse.ok("Logged out successfully", null));
    }
}
