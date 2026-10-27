package com.placementhub.backend.dto;

public class InterviewScheduleRequest {
    private String interviewDate;
    private String interviewTime;
    private String interviewMode;
    private String interviewLocationOrLink;
    private String companyFeedback;

    public InterviewScheduleRequest() {}

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
}
