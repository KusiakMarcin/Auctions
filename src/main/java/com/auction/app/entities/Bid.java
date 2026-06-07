package com.auction.app.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bid {
    Long bidID;
    Long userID;
    Double bidValue;
    Long auctionID;
    private java.sql.Timestamp placedTimestamp;

}
