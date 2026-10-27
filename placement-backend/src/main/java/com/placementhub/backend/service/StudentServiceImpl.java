package com.placementhub.backend.service;

import com.placementhub.backend.dto.LoginRequest;
import com.placementhub.backend.dto.LoginResponse;
import com.placementhub.backend.dto.StudentRegisterRequest;
import com.placementhub.backend.exception.AppException;
import com.placementhub.backend.model.Student;
import com.placementhub.backend.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Student register(StudentRegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new AppException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new AppException("Password is required");
        }
        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            throw new AppException("Full name is required");
        }

        if (studentRepository.findByEmail(request.getEmail().trim().toLowerCase()).isPresent()) {
            throw new AppException("Email already registered");
        }

        Student student = new Student();
        student.setFullName(request.getFullName().trim());
        student.setEmail(request.getEmail().trim().toLowerCase());
        student.setPassword(passwordEncoder.encode(request.getPassword().trim()));
        student.setPhone(request.getPhone());
        student.setDepartment(request.getDepartment());
        student.setGraduationYear(request.getGraduationYear());

        Long id = studentRepository.save(student);
        student.setId(id);
        student.setPassword(null); // Never return password
        return student;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new AppException("Email and password are required");
        }

        Student student = studentRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new AppException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
            throw new AppException("Invalid email or password");
        }

        String token = SessionManager.createSession(student.getId(), "STUDENT", student.getEmail());
        return new LoginResponse(token, "STUDENT", student.getId(), student.getEmail(), student.getFullName());
    }

    @Override
    public Student getProfile(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AppException("Student not found"));
        student.setPassword(null);
        return student;
    }

    @Override
    public Student updateProfile(Long studentId, Student updatedData) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AppException("Student not found"));

        if (updatedData.getFullName() != null && !updatedData.getFullName().trim().isEmpty()) {
            student.setFullName(updatedData.getFullName().trim());
        }
        if (updatedData.getPhone() != null) {
            student.setPhone(updatedData.getPhone().trim());
        }
        if (updatedData.getDepartment() != null) {
            student.setDepartment(updatedData.getDepartment().trim());
        }
        if (updatedData.getGraduationYear() != null) {
            student.setGraduationYear(updatedData.getGraduationYear());
        }

        studentRepository.update(student);
        student.setPassword(null);
        return student;
    }
}
