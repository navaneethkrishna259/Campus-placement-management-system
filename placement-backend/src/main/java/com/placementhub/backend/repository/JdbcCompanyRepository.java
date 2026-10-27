package com.placementhub.backend.repository;

import com.placementhub.backend.model.Company;
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
public class JdbcCompanyRepository implements CompanyRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCompanyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Company> rowMapper = (rs, rowNum) -> {
        Company c = new Company();
        c.setId(rs.getLong("company_id"));
        c.setCompanyName(rs.getString("company_name"));
        c.setEmail(rs.getString("email"));
        c.setPassword(rs.getString("password"));
        c.setPhone(rs.getString("phone"));
        c.setWebsite(rs.getString("website"));
        c.setAddress(rs.getString("address"));
        c.setDescription(rs.getString("description"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    };

    @Override
    public Long save(Company company) {
        String sql = "INSERT INTO companies (company_name, email, password, phone, website, address, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, company.getCompanyName());
            ps.setString(2, company.getEmail());
            ps.setString(3, company.getPassword());
            ps.setString(4, company.getPhone());
            ps.setString(5, company.getWebsite());
            ps.setString(6, company.getAddress());
            ps.setString(7, company.getDescription());
            return ps;
        }, keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
    }

    @Override
    public Optional<Company> findByEmail(String email) {
        String sql = "SELECT * FROM companies WHERE email = ?";
        List<Company> results = jdbcTemplate.query(sql, rowMapper, email);
        return results.stream().findFirst();
    }

    @Override
    public Optional<Company> findById(Long id) {
        String sql = "SELECT * FROM companies WHERE company_id = ?";
        List<Company> results = jdbcTemplate.query(sql, rowMapper, id);
        return results.stream().findFirst();
    }

    @Override
    public void update(Company company) {
        String sql = "UPDATE companies SET company_name = ?, phone = ?, website = ?, address = ?, description = ? WHERE company_id = ?";
        jdbcTemplate.update(sql,
                company.getCompanyName(),
                company.getPhone(),
                company.getWebsite(),
                company.getAddress(),
                company.getDescription(),
                company.getId()
        );
    }
}
