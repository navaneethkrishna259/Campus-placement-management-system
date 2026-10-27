package com.placementhub.backend.repository;

import com.placementhub.backend.model.Student;
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
public class JdbcStudentRepository implements StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcStudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Student> rowMapper = (rs, rowNum) -> {
        Student s = new Student();
        s.setId(rs.getLong("student_id"));
        s.setFullName(rs.getString("full_name"));
        s.setEmail(rs.getString("email"));
        s.setPassword(rs.getString("password"));
        s.setPhone(rs.getString("phone"));
        s.setDepartment(rs.getString("department"));
        s.setGraduationYear(rs.getObject("graduation_year") != null ? rs.getInt("graduation_year") : null);
        s.setCreatedAt(rs.getTimestamp("created_at"));
        return s;
    };

    @Override
    public Long save(Student student) {
        String sql = "INSERT INTO students (full_name, email, password, phone, department, graduation_year) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getFullName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getPassword());
            ps.setString(4, student.getPhone());
            ps.setString(5, student.getDepartment());
            if (student.getGraduationYear() != null) {
                ps.setInt(6, student.getGraduationYear());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            return ps;
        }, keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        String sql = "SELECT * FROM students WHERE email = ?";
        List<Student> results = jdbcTemplate.query(sql, rowMapper, email);
        return results.stream().findFirst();
    }

    @Override
    public Optional<Student> findById(Long id) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        List<Student> results = jdbcTemplate.query(sql, rowMapper, id);
        return results.stream().findFirst();
    }

    @Override
    public void update(Student student) {
        String sql = "UPDATE students SET full_name = ?, phone = ?, department = ?, graduation_year = ? WHERE student_id = ?";
        jdbcTemplate.update(sql,
                student.getFullName(),
                student.getPhone(),
                student.getDepartment(),
                student.getGraduationYear(),
                student.getId()
        );
    }
}
