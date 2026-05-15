package com.auction.app.controlers;

import com.auction.app.entities.Auction;
import com.auction.app.entities.Bid;
import com.auction.app.repositories.AuctionRepository;
import com.auction.app.repositories.BidRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/auctions")
public class AuctionDetailsController {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    public AuctionDetailsController(AuctionRepository auctionRepository, BidRepository bidRepository) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
    }

    @GetMapping("/{id}")
    public String showAuctionDetails(@PathVariable("id") Long id, Model model) {
        //Fetch the auction
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        //Fetch historical bids (ordered by amount DESC)
        List<Bid> bidHistory = bidRepository.findByAuctionId(id);

        //Determine highest bid (if no bids, use starting price)
        Double highestBid = bidHistory.isEmpty()
                ? auction.getStartingBid()
                : bidHistory.get(0).getBidValue();

        model.addAttribute("auction", auction);
        model.addAttribute("bidHistory", bidHistory);
        model.addAttribute("highestBid", highestBid);

        // Pass the raw expiration timestamp for the JavaScript timer
        model.addAttribute("endTime", auction.getExpirationDate().getTime());

        return "auction_page";
    }
}
