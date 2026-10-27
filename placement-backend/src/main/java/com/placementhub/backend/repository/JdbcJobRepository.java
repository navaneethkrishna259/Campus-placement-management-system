package com.placementhub.backend.repository;

import com.placementhub.backend.model.Job;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcJobRepository implements JobRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcJobRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Job> rowMapper = (rs, rowNum) -> {
        Job job = new Job();
        job.setJobId(rs.getLong("job_id"));
        job.setCompanyId(rs.getLong("company_id"));
        job.setJobTitle(rs.getString("job_title"));
        job.setJobDescription(rs.getString("job_description"));
        job.setRequiredSkills(rs.getString("required_skills"));
        job.setSalary(rs.getString("salary"));
        job.setJobLocation(rs.getString("job_location"));
        job.setJobType(rs.getString("job_type"));
        job.setApplicationDeadline(rs.getString("application_deadline"));
        job.setCreatedAt(rs.getTimestamp("created_at"));
        job.setIsActive(rs.getBoolean("is_active"));
        // Check if company_name exists in projection
        try {
            job.setCompanyName(rs.getString("company_name"));
        } catch (Exception ignored) {}
        return job;
    };

    @Override
    public Long save(Job job) {
        String sql = "INSERT INTO jobs (company_id, job_title, job_description, required_skills, salary, job_location, job_type, application_deadline, is_active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, job.getCompanyId());
            ps.setString(2, job.getJobTitle());
            ps.setString(3, job.getJobDescription());
            ps.setString(4, job.getRequiredSkills());
            ps.setString(5, job.getSalary());
            ps.setString(6, job.getJobLocation());
            ps.setString(7, job.getJobType());
            ps.setString(8, job.getApplicationDeadline());
            ps.setBoolean(9, job.getIsActive() != null ? job.getIsActive() : true);
            return ps;
        }, keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
    }

    @Override
    public Optional<Job> findById(Long jobId) {
        String sql = "SELECT j.*, c.company_name FROM jobs j " +
                     "JOIN companies c ON j.company_id = c.company_id " +
                     "WHERE j.job_id = ?";
        List<Job> results = jdbcTemplate.query(sql, rowMapper, jobId);
        return results.stream().findFirst();
    }

    @Override
    public List<Job> findAllActive() {
        String sql = "SELECT j.*, c.company_name FROM jobs j " +
                     "JOIN companies c ON j.company_id = c.company_id " +
                     "WHERE j.is_active = TRUE ORDER BY j.created_at DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<Job> findByCompanyId(Long companyId) {
        String sql = "SELECT j.*, c.company_name FROM jobs j " +
                     "JOIN companies c ON j.company_id = c.company_id " +
                     "WHERE j.company_id = ? ORDER BY j.created_at DESC";
        return jdbcTemplate.query(sql, rowMapper, companyId);
    }

    @Override
    public void updateActiveStatus(Long jobId, boolean isActive) {
        String sql = "UPDATE jobs SET is_active = ? WHERE job_id = ?";
        jdbcTemplate.update(sql, isActive, jobId);
    }
}
