package com.auction.app.dao;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class AuctionCreationDto {
    private String Title;
    private Double StartingBid;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime ExpirationDate;
}
