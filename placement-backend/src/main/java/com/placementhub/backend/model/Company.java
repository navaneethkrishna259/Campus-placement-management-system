package com.placementhub.backend.model;

import java.sql.Timestamp;

/**
 * OOP CONCEPT: INHERITANCE
 * Company extends BaseUser, reusing core credentials and timestamps.
 * OOP CONCEPT: ENCAPSULATION
 * Properties are encapsulated with private access modifiers and validated getters/setters.
 */
public class Company extends BaseUser {
    private String companyName;
    private String website;
    private String address;
    private String description;

    public Company() {
        super();
    }

    public Company(Long id, String companyName, String email, String password, String phone,
                   String website, String address, String description, Timestamp createdAt) {
        super(id, email, password, phone, createdAt);
        this.companyName = companyName;
        this.website = website;
        this.address = address;
        this.description = description;
    }

    // OOP CONCEPT: POLYMORPHISM
    // Overriding getDisplayName() from BaseUser to return company's name
    @Override
    public String getDisplayName() {
        return this.companyName;
    }

    // OOP CONCEPT: POLYMORPHISM
    // Overriding getRole() from BaseUser
    @Override
    public String getRole() {
        return "COMPANY";
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
