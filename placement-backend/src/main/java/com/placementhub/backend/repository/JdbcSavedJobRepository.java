package com.placementhub.backend.repository;

import com.placementhub.backend.model.SavedJob;
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
public class JdbcSavedJobRepository implements SavedJobRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcSavedJobRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<SavedJob> rowMapper = (rs, rowNum) -> {
        SavedJob sj = new SavedJob();
        sj.setSavedJobId(rs.getLong("saved_job_id"));
        sj.setStudentId(rs.getLong("student_id"));
        sj.setJobId(rs.getLong("job_id"));
        sj.setSavedAt(rs.getTimestamp("saved_at"));
        try {
            sj.setJobTitle(rs.getString("job_title"));
            sj.setCompanyName(rs.getString("company_name"));
            sj.setSalary(rs.getString("salary"));
            sj.setJobLocation(rs.getString("job_location"));
            sj.setJobType(rs.getString("job_type"));
            sj.setApplicationDeadline(rs.getString("application_deadline"));
            sj.setJobDescription(rs.getString("job_description"));
            sj.setRequiredSkills(rs.getString("required_skills"));
        } catch (Exception ignored) {}
        return sj;
    };

    @Override
    public Long save(Long studentId, Long jobId) {
        String sql = "INSERT INTO saved_jobs (student_id, job_id) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, studentId);
            ps.setLong(2, jobId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
    }

    @Override
    public void delete(Long studentId, Long jobId) {
        String sql = "DELETE FROM saved_jobs WHERE student_id = ? AND job_id = ?";
        jdbcTemplate.update(sql, studentId, jobId);
    }

    @Override
    public Optional<SavedJob> findByStudentAndJob(Long studentId, Long jobId) {
        String sql = "SELECT sj.*, j.job_title, j.salary, j.job_location, j.job_type, j.application_deadline, " +
                     "j.job_description, j.required_skills, c.company_name " +
                     "FROM saved_jobs sj " +
                     "JOIN jobs j ON sj.job_id = j.job_id " +
                     "JOIN companies c ON j.company_id = c.company_id " +
                     "WHERE sj.student_id = ? AND sj.job_id = ?";
        List<SavedJob> results = jdbcTemplate.query(sql, rowMapper, studentId, jobId);
        return results.stream().findFirst();
    }

    @Override
    public List<SavedJob> findByStudentId(Long studentId) {
        String sql = "SELECT sj.*, j.job_title, j.salary, j.job_location, j.job_type, j.application_deadline, " +
                     "j.job_description, j.required_skills, c.company_name " +
                     "FROM saved_jobs sj " +
                     "JOIN jobs j ON sj.job_id = j.job_id " +
                     "JOIN companies c ON j.company_id = c.company_id " +
                     "WHERE sj.student_id = ? ORDER BY sj.saved_at DESC";
        return jdbcTemplate.query(sql, rowMapper, studentId);
    }
}
