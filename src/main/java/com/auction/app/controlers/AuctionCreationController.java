package com.auction.app.controlers;


import com.auction.app.dto.AuctionCreationDto;
import com.auction.app.entities.Auction;
import com.auction.app.repositories.AuctionRepository;
import com.auction.app.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/auctions")
public class AuctionCreationController {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    public AuctionCreationController(AuctionRepository auctionRepository, UserRepository userRepository) {
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listAuctions(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            Model model) {

        List<Auction> auctions;

        // Determine which repository filter method to run based on incoming params
        if(search!=null || category!= null || minPrice!=null || maxPrice!=null)
            auctions = auctionRepository.findWithFilters(search,category,minPrice,maxPrice);
        else auctions = auctionRepository.findAll();

        model.addAttribute("auctions", auctions);
        model.addAttribute("selectedSearch", search);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedMinPrice", minPrice);
        model.addAttribute("selectedMaxPrice", maxPrice);

        return "auction_list"; // Returns the list view template
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("auctionDto", new AuctionCreationDto());
        return "create_auction";
    }

    @PostMapping("/new")
    public String processCreateAuction(@ModelAttribute("auctionDto") AuctionCreationDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();



        // Logic to save the auction via your repository
        auctionRepository.save(dto, userRepository.getUserID());

        return "redirect:/auctions?created=true";
    }
}
