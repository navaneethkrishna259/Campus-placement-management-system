package com.placementhub.backend.model;

import java.sql.Timestamp;

/**
 * OOP CONCEPT: ENCAPSULATION
 * Model representing job postings with private state and clear accessors.
 */
public class Job {
    private Long jobId;
    private Long companyId;
    private String companyName; // Populated in joined queries for convenience
    private String jobTitle;
    private String jobDescription;
    private String requiredSkills;
    private String salary;
    private String jobLocation;
    private String jobType;
    private String applicationDeadline;
    private Timestamp createdAt;
    private Boolean isActive;
    private Boolean isSaved; // Computed flag for student context

    public Job() {
        this.isActive = true;
    }

    public Job(Long jobId, Long companyId, String jobTitle, String jobDescription,
               String requiredSkills, String salary, String jobLocation, String jobType,
               String applicationDeadline, Timestamp createdAt, Boolean isActive) {
        this.jobId = jobId;
        this.companyId = companyId;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.requiredSkills = requiredSkills;
        this.salary = salary;
        this.jobLocation = jobLocation;
        this.jobType = jobType;
        this.applicationDeadline = applicationDeadline;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(String applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(Boolean isSaved) {
        this.isSaved = isSaved;
    }
}
