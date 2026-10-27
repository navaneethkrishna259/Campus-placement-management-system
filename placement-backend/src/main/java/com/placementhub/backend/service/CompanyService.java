package com.placementhub.backend.service;

import com.placementhub.backend.dto.CompanyRegisterRequest;
import com.placementhub.backend.dto.LoginRequest;
import com.placementhub.backend.dto.LoginResponse;
import com.placementhub.backend.model.Company;

/**
 * OOP CONCEPT: ABSTRACTION
 * Interface defining the business logic contract for company operations.
 */
public interface CompanyService {
    Company register(CompanyRegisterRequest request);
    LoginResponse login(LoginRequest request);
    Company getProfile(Long companyId);
    Company updateProfile(Long companyId, Company updatedData);
}
