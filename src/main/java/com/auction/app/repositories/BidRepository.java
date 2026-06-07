package com.auction.app.repositories;

import com.auction.app.entities.Bid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.stereotype.Repository;
import com.auction.app.dto.BidCreationDto;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BidRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    @Autowired
    public BidRepository(JdbcTemplate jdbcTemplate, UserRepository userRepository){
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }


    public int save(BidCreationDto dto, Long auctionID)
    {
        String sql = "insert into \"Bids\"(\"User_ID_Users\",\"Auction_ID_Auction\",\"Bid_Value\",\"Placed_Timestamp\") Values(?,?,?,?)";
        return jdbcTemplate.update(
                sql,
                userRepository.getUserID(),
                auctionID,
                dto.getBidValue(),
                LocalDateTime.now()

        );
    }

    public List<Bid> findByAuctionId(Long id) {
        // We order by Bid_Value DESC so the current highest bid is always the first element (index 0)
        String sql = "SELECT * FROM \"Bids\" WHERE \"Auction_ID_Auction\" = ? ORDER BY \"Bid_Value\" DESC";

        return jdbcTemplate.query(sql, bidRowMapper, id);
    }

    public List<Bid> findByOwner(Long id) {
        // We order by Bid_Value DESC so the current highest bid is always the first element (index 0)
        String sql = "SELECT * FROM \"Bids\" WHERE \"User_ID_Users\" = ? ORDER BY \"Bid_Value\" DESC";

        return jdbcTemplate.query(sql, bidRowMapper, id);
    }

    private final RowMapper<Bid> bidRowMapper = (rs, rowNum) -> {
        Bid bid = new Bid();

        bid.setUserID(rs.getLong("User_ID_Users"));
        bid.setAuctionID(rs.getLong("Auction_ID_Auction"));
        bid.setBidValue(rs.getDouble("Bid_Value"));
        bid.setPlacedTimestamp(rs.getTimestamp("Placed_Timestamp"));



        return bid;
    };
}
