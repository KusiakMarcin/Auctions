package com.auction.app.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.auction.app.entities.User;

import java.math.BigDecimal;
import java.util.List;

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


}