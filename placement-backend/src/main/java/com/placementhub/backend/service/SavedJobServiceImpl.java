package com.placementhub.backend.service;

import com.placementhub.backend.exception.AppException;
import com.placementhub.backend.model.SavedJob;
import com.placementhub.backend.repository.JobRepository;
import com.placementhub.backend.repository.SavedJobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedJobServiceImpl implements SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;

    public SavedJobServiceImpl(SavedJobRepository savedJobRepository, JobRepository jobRepository) {
        this.savedJobRepository = savedJobRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    public void saveJob(Long studentId, Long jobId) {
        jobRepository.findById(jobId)
                .orElseThrow(() -> new AppException("Job not found"));

        if (savedJobRepository.findByStudentAndJob(studentId, jobId).isPresent()) {
            throw new AppException("Job already saved");
        }

        savedJobRepository.save(studentId, jobId);
    }

    @Override
    public void removeSavedJob(Long studentId, Long jobId) {
        savedJobRepository.delete(studentId, jobId);
    }

    @Override
    public List<SavedJob> getSavedJobs(Long studentId) {
        return savedJobRepository.findByStudentId(studentId);
    }
}
