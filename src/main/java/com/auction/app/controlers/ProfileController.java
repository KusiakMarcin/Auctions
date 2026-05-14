package com.auction.app.controlers;

import com.auction.app.entities.User;
import com.auction.app.repositories.AuctionRepository;
import com.auction.app.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;


    public ProfileController(UserRepository userRepository, AuctionRepository auctionRepository) {
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;

    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        //Get the logged-in user's details
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetchdata
        model.addAttribute("user", user);


        return "profile_page";
    }
}