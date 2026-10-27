package com.placementhub.backend.service;

import com.placementhub.backend.dto.LoginRequest;
import com.placementhub.backend.dto.LoginResponse;
import com.placementhub.backend.dto.StudentRegisterRequest;
import com.placementhub.backend.model.Student;

/**
 * OOP CONCEPT: ABSTRACTION
 * Interface defining the business logic contract for student operations.
 */
public interface StudentService {
    Student register(StudentRegisterRequest request);
    LoginResponse login(LoginRequest request);
    Student getProfile(Long studentId);
    Student updateProfile(Long studentId, Student updatedData);
}
