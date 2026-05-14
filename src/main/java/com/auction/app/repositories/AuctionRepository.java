package com.auction.app.repositories;


import com.auction.app.dao.AuctionCreationDto;
import com.auction.app.entities.Auction;
import com.auction.app.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.auction.app.entities.User;

import java.sql.Timestamp;

@Repository
public class AuctionRepository
{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuctionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(AuctionCreationDto dto, Long id)
    {
        String sql ="insert into auctions (\"Title\",\"User_ID_Users\",\"Starting_Bid\",\"Expiration_date\") Values (?,?,?,?);";
        Timestamp timestamp = Timestamp.valueOf(dto.getExpirationDate());
        return jdbcTemplate.update(sql,
                dto.getTitle(),
                id,
                dto.getStartingBid(),
                timestamp);



    }


}
