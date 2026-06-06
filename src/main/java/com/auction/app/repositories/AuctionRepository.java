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
import java.util.List;
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

        String sql ="insert into auctions (\"Title\",\"User_ID_Users\",\"Starting_Bid\",\"Expiration_date\",\"Category\") Values (?,?,?,?,?::public.auction_category);";
        Timestamp timestamp = Timestamp.valueOf(dto.getExpirationDate());
        return jdbcTemplate.update(sql,
                dto.getTitle(),
                id,
                dto.getStartingBid(),
                timestamp,
                dto.getCategory()
                );



    }

    private final RowMapper<Auction> AuctionRowMapper = (rs, rowNum) -> {
        Auction auction = new Auction();
        auction.setAuctionID(rs.getLong("Auction_ID"));
        auction.setTitle(rs.getString("Title"));
        auction.setCategory(rs.getString("Category"));
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

    public List<Auction> findAll()
    {
        String sql = "SELECT * FROM public.Auctions";
        return jdbcTemplate.query(sql, AuctionRowMapper);
    }

    public List<Auction> findByTitleAndCategory(String title, String category) {
        String sql = "SELECT * FROM public.Auctions WHERE \"Title\" ILIKE ? AND \"Category\" = ?::auction_category";
        return jdbcTemplate.query(sql, AuctionRowMapper, "%" + title + "%", category);
    }



    public List<Auction> findByCategory(String category) {
        String sql = "SELECT * FROM public.Auctions WHERE \"Category\" = ?::auction_category";
        return jdbcTemplate.query(sql, AuctionRowMapper, category);
    }

    public List<Auction> searchByTitle(String search) {

        String sql = "SELECT * FROM public.Auctions WHERE \"Title\" ILIKE ?";
        return jdbcTemplate.query(sql, AuctionRowMapper, search);
    }
    public List<Auction> findWithFilters(String title, String category, Double minPrice, Double maxPrice) {
        StringBuilder sql = new StringBuilder("SELECT * FROM public.Auctions WHERE 1=1");
        List<Object> params = new java.util.ArrayList<>();

        if (title != null && !title.trim().isEmpty()) {
            sql.append(" AND \"Title\" ILIKE ?");
            params.add("%" + title + "%");
        }

        if (category != null && !category.isEmpty()) {
            sql.append(" AND \"Category\" = ?::auction_category");
            params.add(category);
        }

        if (minPrice != null) {
            // Checking against Current_Highest_Bid, fallback to Starting_Bid if null
            sql.append(" AND COALESCE(\"Current_Highest_Bid\", \"Starting_Bid\") >= ?");
            params.add(minPrice);
        }

        if (maxPrice != null) {
            sql.append(" AND COALESCE(\"Current_Highest_Bid\", \"Starting_Bid\") <= ?");
            params.add(maxPrice);
        }

        return jdbcTemplate.query(sql.toString(), AuctionRowMapper, params.toArray());
    }
}
