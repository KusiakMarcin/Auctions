package com.auction.app.controlers;

import com.auction.app.entities.User;
import com.auction.app.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class WalletController {

    private final UserRepository userRepository;

    public WalletController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/wallet")
    public String handleWalletTransaction(@RequestParam("action") String action,
                                          @RequestParam("amount") Double amount,
                                          RedirectAttributes redirectAttributes) {
        // 1. Fetch current logged-in user profile entity details
        // Note: Using your existing repository lookup strategy
        Long currentUserId = userRepository.getUserID();
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Authenticated user context missing"));

        // 2. Compute transactional operations
        if ("deposit".equalsIgnoreCase(action)) {
            user.setBalance(user.getBalance() + amount);
            userRepository.updateBalance(user.getId(), user.getBalance());
            redirectAttributes.addFlashAttribute("successMessage", "Deposited " + amount + " safely!");

        } else if ("withdraw".equalsIgnoreCase(action)) {
            if (user.getBalance() >= amount) {
                user.setBalance(user.getBalance() - amount);
                userRepository.updateBalance(user.getId(), user.getBalance());
                redirectAttributes.addFlashAttribute("successMessage", "Withdrew " + amount + " successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Insufficient wallet balances found.");
            }
        }

        // 3. Return to current updated user dashboard workspace view
        return "redirect:/profile";
    }
}
