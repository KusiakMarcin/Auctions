package com.auction.app.controlers;

import com.auction.app.dto.BidCreationDto;
import com.auction.app.repositories.BidRepository;
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

    public BidCreationController(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    @PostMapping("/{id}/bid")
    public String processBid(@PathVariable("id") Long id,
                             @ModelAttribute BidCreationDto bidDto,
                             RedirectAttributes redirectAttributes) {

        // 1. Save the bid using your repository
        // Note: Your repository already handles getting the UserID internally
        bidRepository.save(bidDto, id);

        // 2. Add a success message (optional but good for UX)
        redirectAttributes.addFlashAttribute("message", "Bid placed successfully!");

        // 3. Redirect back to the specific auction page
        return "redirect:/auctions/" + id;
    }
}