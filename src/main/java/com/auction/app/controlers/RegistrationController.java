package com.auction.app.controlers;


import com.auction.app.entities.User;
import com.auction.app.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.auction.app.dto.UserRegistrationDto;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @GetMapping("/register")
    public String showForm(Model model) {
        // We pass an empty DTO to the form to bind data to it
        model.addAttribute("userDto", new UserRegistrationDto());
        return "register_page";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("userDto") UserRegistrationDto dto) {





        User newUser = new User();
        newUser.setEmail(dto.getEmail());

        String Hash = passwordEncoder.encode(dto.getPassword());
        newUser.setPassword(Hash);
        newUser.setName(dto.getName());
        newUser.setLastName(dto.getLastName());
        newUser.setBalance(0.0);


        userRepository.save(newUser);

        return "redirect:/login?registered=true";
    }
}