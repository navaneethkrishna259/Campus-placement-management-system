package com.placementhub.backend.model;

import java.sql.Timestamp;

/**
 * OOP CONCEPT: INHERITANCE
 * Student extends BaseUser, inheriting id, email, password, phone, and createdAt.
 * OOP CONCEPT: ENCAPSULATION
 * Fields are private and accessed via well-defined getters and setters.
 */
public class Student extends BaseUser {
    private String fullName;
    private String department;
    private Integer graduationYear;

    public Student() {
        super();
    }

    public Student(Long id, String fullName, String email, String password, String phone,
                   String department, Integer graduationYear, Timestamp createdAt) {
        super(id, email, password, phone, createdAt);
        this.fullName = fullName;
        this.department = department;
        this.graduationYear = graduationYear;
    }

    // OOP CONCEPT: POLYMORPHISM
    // Overriding getDisplayName() from BaseUser to return student's full name
    @Override
    public String getDisplayName() {
        return this.fullName;
    }

    // OOP CONCEPT: POLYMORPHISM
    // Overriding getRole() from BaseUser
    @Override
    public String getRole() {
        return "STUDENT";
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }
}
