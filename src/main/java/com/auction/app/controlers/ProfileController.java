package com.auction.app.controlers;

import com.auction.app.entities.Auction;
import com.auction.app.entities.Bid;
import com.auction.app.entities.User;
import com.auction.app.repositories.AuctionRepository;
import com.auction.app.repositories.BidRepository;
import com.auction.app.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;


    public ProfileController(UserRepository userRepository, AuctionRepository auctionRepository, BidRepository bidRepository) {
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        //Get the logged-in user's details
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Long id = userRepository.getUserID();
        List<Auction> auctions = auctionRepository.findByOwner(id);
        List<Bid> bids = bidRepository.findByOwner(id);

        // Fetchdata
        model.addAttribute("user", user);
        model.addAttribute("myAuctions",auctions);
        model.addAttribute("myBids",bids);


        return "profile_page";
    }
}