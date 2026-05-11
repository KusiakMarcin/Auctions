package com.auction.app.repositories;

import com.auction.app.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.auction.app.entities.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public int save(User user) {
        String sql = "INSERT INTO \"Users\" (\"Email\", \"Password\", \"Name\", \"Last_Name\", \"Saldo\", \"Role\") VALUES (?, ?, ?, ?, ?, \'USER\')";
        return jdbcTemplate.update(sql,
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                user.getLastName(),
                user.getBalance()

        );
    }


    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, email);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            // JdbcTemplate throws an exception if 0 rows are found
            return Optional.empty();
        }
    }

    /**
     * Maps the SQL ResultSet to the User object.
     */
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("User_ID"));
        user.setEmail(rs.getString("Email"));
        user.setPassword(rs.getString("Password"));
        user.setName(rs.getString("Name"));
        user.setLastName(rs.getString("Last_Name"));
        user.setBalance(rs.getDouble("Saldo"));
        user.setRole(rs.getString("Role"));
        return user;
    };
}