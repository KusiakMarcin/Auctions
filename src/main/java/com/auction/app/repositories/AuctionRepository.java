package com.auction.app.repositories;


import com.auction.app.dto.AuctionCreationDto;
import com.auction.app.entities.Auction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ext.jdk8.OptionalIntDeserializer;

import java.sql.Timestamp;
import java.util.Optional;

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

    private final RowMapper<Auction> AuctionRowMapper = (rs, rowNum) -> {
        Auction auction = new Auction();
        auction.setAuctionID(rs.getLong("Auction_ID"));
        auction.setTitle(rs.getString("Title"));
        auction.setPaymentID(rs.getLong("Payment_ID_Payments"));
        auction.setUserID(rs.getLong("User_ID_Users"));
        auction.setStartingBid(rs.getDouble("Starting_Bid"));
        auction.setExpirationDate(rs.getTimestamp("Expiration_date"));
        auction.setCurrentHighestBid(rs.getDouble("Current_Highest_Bid"));
        return auction;
    };


    public Optional<Auction> findById(Long id) {

        String sql = "Select * from auctions where \"Auction_ID\" = ?";

        try {
            Auction auction = jdbcTemplate.queryForObject(sql,AuctionRowMapper,id);
            return Optional.of(auction);
        } catch (EmptyResultDataAccessException e) {

            return Optional.empty();
        }
    }
}
