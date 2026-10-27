package com.placementhub.backend.model;

import java.sql.Timestamp;

/**
 * OOP CONCEPT: ENCAPSULATION
 * Model representing job applications and interview schedules.
 */
public class Application {
    private Long applicationId;
    private Long studentId;
    private Long jobId;
    private Timestamp appliedAt;
    private String status; // APPLIED, SHORTLISTED, REJECTED, INTERVIEW_SCHEDULED
    private String interviewDate;
    private String interviewTime;
    private String interviewMode;
    private String interviewLocationOrLink;
    private String companyFeedback;

    // Additional display fields from joins
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private String department;
    private Integer graduationYear;
    private String jobTitle;
    private String companyName;

    public Application() {
        this.status = "APPLIED";
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Timestamp getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Timestamp appliedAt) {
        this.appliedAt = appliedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(String interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(String interviewTime) {
        this.interviewTime = interviewTime;
    }

    public String getInterviewMode() {
        return interviewMode;
    }

    public void setInterviewMode(String interviewMode) {
        this.interviewMode = interviewMode;
    }

    public String getInterviewLocationOrLink() {
        return interviewLocationOrLink;
    }

    public void setInterviewLocationOrLink(String interviewLocationOrLink) {
        this.interviewLocationOrLink = interviewLocationOrLink;
    }

    public String getCompanyFeedback() {
        return companyFeedback;
    }

    public void setCompanyFeedback(String companyFeedback) {
        this.companyFeedback = companyFeedback;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
