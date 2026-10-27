package com.placementhub.backend.service;

import com.placementhub.backend.dto.CompanyRegisterRequest;
import com.placementhub.backend.dto.LoginRequest;
import com.placementhub.backend.dto.LoginResponse;
import com.placementhub.backend.exception.AppException;
import com.placementhub.backend.model.Company;
import com.placementhub.backend.repository.CompanyRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public CompanyServiceImpl(CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Company register(CompanyRegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new AppException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new AppException("Password is required");
        }
        if (request.getCompanyName() == null || request.getCompanyName().trim().isEmpty()) {
            throw new AppException("Company name is required");
        }

        if (companyRepository.findByEmail(request.getEmail().trim().toLowerCase()).isPresent()) {
            throw new AppException("Email already registered");
        }

        Company company = new Company();
        company.setCompanyName(request.getCompanyName().trim());
        company.setEmail(request.getEmail().trim().toLowerCase());
        company.setPassword(passwordEncoder.encode(request.getPassword().trim()));
        company.setPhone(request.getPhone());
        company.setWebsite(request.getWebsite());
        company.setAddress(request.getAddress());
        company.setDescription(request.getDescription());

        Long id = companyRepository.save(company);
        company.setId(id);
        company.setPassword(null);
        return company;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new AppException("Email and password are required");
        }

        Company company = companyRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new AppException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), company.getPassword())) {
            throw new AppException("Invalid email or password");
        }

        String token = SessionManager.createSession(company.getId(), "COMPANY", company.getEmail());
        return new LoginResponse(token, "COMPANY", company.getId(), company.getEmail(), company.getCompanyName());
    }

    @Override
    public Company getProfile(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new AppException("Company not found"));
        company.setPassword(null);
        return company;
    }

    @Override
    public Company updateProfile(Long companyId, Company updatedData) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new AppException("Company not found"));

        if (updatedData.getCompanyName() != null && !updatedData.getCompanyName().trim().isEmpty()) {
            company.setCompanyName(updatedData.getCompanyName().trim());
        }
        if (updatedData.getPhone() != null) {
            company.setPhone(updatedData.getPhone().trim());
        }
        if (updatedData.getWebsite() != null) {
            company.setWebsite(updatedData.getWebsite().trim());
        }
        if (updatedData.getAddress() != null) {
            company.setAddress(updatedData.getAddress().trim());
        }
        if (updatedData.getDescription() != null) {
            company.setDescription(updatedData.getDescription().trim());
        }

        companyRepository.update(company);
        company.setPassword(null);
        return company;
    }
}
