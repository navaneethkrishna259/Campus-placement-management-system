package com.placementhub.backend.service;

import com.placementhub.backend.dto.JobPostRequest;
import com.placementhub.backend.exception.AppException;
import com.placementhub.backend.model.Job;
import com.placementhub.backend.model.SavedJob;
import com.placementhub.backend.repository.JobRepository;
import com.placementhub.backend.repository.SavedJobRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final SavedJobRepository savedJobRepository;

    public JobServiceImpl(JobRepository jobRepository, SavedJobRepository savedJobRepository) {
        this.jobRepository = jobRepository;
        this.savedJobRepository = savedJobRepository;
    }

    @Override
    public Job postJob(Long companyId, JobPostRequest request) {
        if (request.getJobTitle() == null || request.getJobTitle().trim().isEmpty()) {
            throw new AppException("Job title is required");
        }
        if (request.getJobDescription() == null || request.getJobDescription().trim().isEmpty()) {
            throw new AppException("Job description is required");
        }
        if (request.getRequiredSkills() == null || request.getRequiredSkills().trim().isEmpty()) {
            throw new AppException("Required skills are required");
        }

        Job job = new Job();
        job.setCompanyId(companyId);
        job.setJobTitle(request.getJobTitle().trim());
        job.setJobDescription(request.getJobDescription().trim());
        job.setRequiredSkills(request.getRequiredSkills().trim());
        job.setSalary(request.getSalary());
        job.setJobLocation(request.getJobLocation());
        job.setJobType(request.getJobType());
        job.setApplicationDeadline(request.getApplicationDeadline());
        job.setIsActive(true);

        Long jobId = jobRepository.save(job);
        job.setJobId(jobId);
        return job;
    }

    @Override
    public List<Job> getActiveJobs(Long studentId) {
        List<Job> activeJobs = jobRepository.findAllActive();
        if (studentId != null) {
            Set<Long> savedJobIds = savedJobRepository.findByStudentId(studentId).stream()
                    .map(SavedJob::getJobId)
                    .collect(Collectors.toSet());
            for (Job job : activeJobs) {
                job.setIsSaved(savedJobIds.contains(job.getJobId()));
            }
        }
        return activeJobs;
    }

    @Override
    public List<Job> getCompanyJobs(Long companyId) {
        return jobRepository.findByCompanyId(companyId);
    }

    @Override
    public Job getJobById(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new AppException("Job not found"));
    }

    @Override
    public void cancelJob(Long companyId, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new AppException("Job not found"));

        if (!job.getCompanyId().equals(companyId)) {
            throw new AppException("You are not authorized to cancel this job");
        }

        jobRepository.updateActiveStatus(jobId, false);
    }
}
