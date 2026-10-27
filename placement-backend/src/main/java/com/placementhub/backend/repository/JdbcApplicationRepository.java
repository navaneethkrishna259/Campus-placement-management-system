package com.placementhub.backend.repository;

import com.placementhub.backend.model.Application;
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
public class JdbcApplicationRepository implements ApplicationRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcApplicationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Application> rowMapper = (rs, rowNum) -> {
        Application app = new Application();
        app.setApplicationId(rs.getLong("application_id"));
        app.setStudentId(rs.getLong("student_id"));
        app.setJobId(rs.getLong("job_id"));
        app.setAppliedAt(rs.getTimestamp("applied_at"));
        app.setStatus(rs.getString("status"));
        app.setInterviewDate(rs.getString("interview_date"));
        app.setInterviewTime(rs.getString("interview_time"));
        app.setInterviewMode(rs.getString("interview_mode"));
        app.setInterviewLocationOrLink(rs.getString("interview_location_or_link"));
        app.setCompanyFeedback(rs.getString("company_feedback"));

        try {
            app.setStudentName(rs.getString("student_name"));
        } catch (Exception ignored) {}
        try {
            app.setStudentEmail(rs.getString("student_email"));
        } catch (Exception ignored) {}
        try {
            app.setStudentPhone(rs.getString("student_phone"));
        } catch (Exception ignored) {}
        try {
            app.setDepartment(rs.getString("department"));
        } catch (Exception ignored) {}
        try {
            if (rs.getObject("graduation_year") != null) {
                app.setGraduationYear(rs.getInt("graduation_year"));
            }
        } catch (Exception ignored) {}
        try {
            app.setJobTitle(rs.getString("job_title"));
        } catch (Exception ignored) {}
        try {
            app.setCompanyName(rs.getString("company_name"));
        } catch (Exception ignored) {}

        return app;
    };

    @Override
    public Long save(Application application) {
        String sql = "INSERT INTO applications (student_id, job_id, status) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, application.getStudentId());
            ps.setLong(2, application.getJobId());
            ps.setString(3, application.getStatus() != null ? application.getStatus() : "APPLIED");
            return ps;
        }, keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
    }

    @Override
    public Optional<Application> findById(Long applicationId) {
        String sql = "SELECT a.*, s.full_name AS student_name, s.email AS student_email, s.phone AS student_phone, " +
                     "s.department, s.graduation_year, j.job_title, c.company_name " +
                     "FROM applications a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN jobs j ON a.job_id = j.job_id " +
                     "JOIN companies c ON j.company_id = c.company_id " +
                     "WHERE a.application_id = ?";
        List<Application> results = jdbcTemplate.query(sql, rowMapper, applicationId);
        return results.stream().findFirst();
    }

    @Override
    public Optional<Application> findByStudentAndJob(Long studentId, Long jobId) {
        String sql = "SELECT a.*, s.full_name AS student_name, s.email AS student_email, s.phone AS student_phone, " +
                     "s.department, s.graduation_year, j.job_title, c.company_name " +
                     "FROM applications a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN jobs j ON a.job_id = j.job_id " +
                     "JOIN companies c ON j.company_id = c.company_id " +
                     "WHERE a.student_id = ? AND a.job_id = ?";
        List<Application> results = jdbcTemplate.query(sql, rowMapper, studentId, jobId);
        return results.stream().findFirst();
    }

    @Override
    public List<Application> findByStudentId(Long studentId) {
        String sql = "SELECT a.*, s.full_name AS student_name, s.email AS student_email, s.phone AS student_phone, " +
                     "s.department, s.graduation_year, j.job_title, c.company_name " +
                     "FROM applications a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN jobs j ON a.job_id = j.job_id " +
                     "JOIN companies c ON j.company_id = c.company_id " +
                     "WHERE a.student_id = ? ORDER BY a.applied_at DESC";
        return jdbcTemplate.query(sql, rowMapper, studentId);
    }

    @Override
    public List<Application> findByCompanyId(Long companyId) {
        String sql = "SELECT a.*, s.full_name AS student_name, s.email AS student_email, s.phone AS student_phone, " +
                     "s.department, s.graduation_year, j.job_title, c.company_name " +
                     "FROM applications a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN jobs j ON a.job_id = j.job_id " +
                     "JOIN companies c ON j.company_id = c.company_id " +
                     "WHERE j.company_id = ? ORDER BY a.applied_at DESC";
        return jdbcTemplate.query(sql, rowMapper, companyId);
    }

    @Override
    public void updateStatus(Long applicationId, String status) {
        String sql = "UPDATE applications SET status = ? WHERE application_id = ?";
        jdbcTemplate.update(sql, status, applicationId);
    }

    @Override
    public void updateInterview(Long applicationId, String date, String time, String mode, String locationOrLink, String feedback) {
        String sql = "UPDATE applications SET status = 'INTERVIEW_SCHEDULED', interview_date = ?, interview_time = ?, " +
                     "interview_mode = ?, interview_location_or_link = ?, company_feedback = ? WHERE application_id = ?";
        jdbcTemplate.update(sql, date, time, mode, locationOrLink, feedback, applicationId);
    }
}
