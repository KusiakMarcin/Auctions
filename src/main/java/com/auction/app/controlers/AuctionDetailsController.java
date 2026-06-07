package com.auction.app.controlers;

import com.auction.app.entities.Auction;
import com.auction.app.entities.Bid;
import com.auction.app.entities.User;
import com.auction.app.repositories.AuctionRepository;
import com.auction.app.repositories.BidRepository;
import com.auction.app.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/auctions")
public class AuctionDetailsController {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;

    public AuctionDetailsController(AuctionRepository auctionRepository, BidRepository bidRepository, UserRepository userRepository) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public String showAuctionDetails(@PathVariable("id") Long id, Model model) {
        //Fetch the auction
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        //Fetch historical bids
        List<Bid> bidHistory = bidRepository.findByAuctionId(id);

        //Determine highest bid
        Double highestBid = bidHistory.isEmpty()
                ? auction.getStartingBid()
                : bidHistory.get(0).getBidValue();

        Map<Long,User> users = new HashMap<>();
        for(Bid bid: bidHistory)
        {
            if(!users.containsKey(bid.getUserID()))
                users.put(bid.getUserID(),userRepository.findById(bid.getUserID()).get());
        }

        model.addAttribute("auction", auction);
        model.addAttribute("bidHistory", bidHistory);
        model.addAttribute("highestBid", highestBid);
        model.addAttribute("userData",users);

        // Pass the raw expiration timestamp for the JavaScript timer
        model.addAttribute("endTime", auction.getExpirationDate().getTime());

        return "auction_page";
    }
}
