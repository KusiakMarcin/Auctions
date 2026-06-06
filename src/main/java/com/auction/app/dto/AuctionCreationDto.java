package com.auction.app.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class AuctionCreationDto {
    private String Title;
    private Double StartingBid;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime ExpirationDate;
    private String category;
}
