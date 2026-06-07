package com.auction.app.controlers;

import com.auction.app.dto.BidCreationDto;
import com.auction.app.entities.Auction;
import com.auction.app.repositories.AuctionRepository;
import com.auction.app.repositories.BidRepository;
import com.auction.app.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auctions")
public class BidCreationController {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    public BidCreationController(BidRepository bidRepository, AuctionRepository auctionRepository,UserRepository userRepository) {

        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/{id}/bid")
    public String processBid(@PathVariable("id") Long id,
                             @ModelAttribute BidCreationDto bidDto,
                             RedirectAttributes redirectAttributes) {


        // 1. Fetch the auction to check its rules
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        Long currentUserId = userRepository.getUserID();
        Double incomingBidValue = bidDto.getBidValue();

        // Check if the auction has expired
        if (auction.getExpirationDate().before(new java.util.Date()))
        {
            redirectAttributes.addFlashAttribute("errorMessage", "Bidding failed: This auction has already expired.");
            return "redirect:/auctions/" + id;
        }

        // Check if the user is biding on their own item
        if (auction.getUserID().equals(currentUserId))
        {
            redirectAttributes.addFlashAttribute("errorMessage", "Bidding failed: You cannot bid on your own auction.");
            return "redirect:/auctions/" + id;
        }

        if(auction.getCurrentHighestBid() >= incomingBidValue)
        {
            redirectAttributes.addFlashAttribute("errorMessage",
                    String.format("Bidding failed: Your bid must be higher than %s.",
                            java.text.NumberFormat.getCurrencyInstance().format(auction.getCurrentHighestBid())));
            return "redirect:/auctions/" + id;
        }

        if(incomingBidValue > userRepository.findById(currentUserId).get().getBalance())
        {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "You do not have enough funds");
            return "redirect:/auctions/" + id;
        }

        bidRepository.save(bidDto, id);


        // 2. Add a success message (optional but good for UX)
        redirectAttributes.addFlashAttribute("message", "Bid placed successfully!");

        // 3. Redirect back to the specific auction page
        return "redirect:/auctions/" + id;
    }
}