package com.auction.app.controlers;


import com.auction.app.dto.AuctionCreationDto;
import com.auction.app.repositories.AuctionRepository;
import com.auction.app.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller

public class AuctionCreationController {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    public AuctionCreationController(AuctionRepository auctionRepository, UserRepository userRepository) {
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/auctions/new")
    public String showCreateForm(Model model) {
        model.addAttribute("auctionDto", new AuctionCreationDto());
        return "create_auction";
    }

    @PostMapping("/auctions/new")
    public String processCreateAuction(@ModelAttribute("auctionDto") AuctionCreationDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();



        // Logic to save the auction via your repository
        auctionRepository.save(dto, userRepository.getUserID());

        return "redirect:/auctions?created=true";
    }
}
