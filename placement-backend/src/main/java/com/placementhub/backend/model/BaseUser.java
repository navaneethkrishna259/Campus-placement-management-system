package com.placementhub.backend.model;

import java.sql.Timestamp;

/**
 * OOP CONCEPT: INHERITANCE & ENCAPSULATION
 * BaseUser serves as the abstract superclass representing shared user attributes
 * and common user behaviors for both Student and Company in the recruitment platform.
 */
public abstract class BaseUser {
    // Encapsulation: Private member variables protect the internal state from uncontrolled access
    private Long id;
    private String email;
    private String password;
    private String phone;
    private Timestamp createdAt;

    public BaseUser() {
    }

    public BaseUser(Long id, String email, String password, String phone, Timestamp createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    // OOP CONCEPT: POLYMORPHISM
    // Abstract method defining a common contract. Subclasses (Student and Company)
    // override getDisplayName() and getRole() to supply entity-specific values.
    public abstract String getDisplayName();

    public abstract String getRole();

    // Getters and Setters demonstrating encapsulation
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
