package com.auction.app.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auction {
        private Long ID;
        private String Title;
        private Long PaymentID;
        private Long UserID;
        private Double StartingBid;
        private Double CurrentHighestBid;
        private Timestamp ExpirationDate;
}
