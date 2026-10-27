package com.placementhub.backend.repository;

import com.placementhub.backend.model.Company;
import java.util.Optional;

/**
 * OOP CONCEPT: ABSTRACTION
 * Interface declaring data access operations for Company entity.
 */
public interface CompanyRepository {
    Long save(Company company);
    Optional<Company> findByEmail(String email);
    Optional<Company> findById(Long id);
    void update(Company company);
}
