package com.auction.app.controlers;


import com.auction.app.entities.User;
import com.auction.app.repositories.UserRepository;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.auction.app.dto.UserRegistrationDto;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


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
    public String processRegister(@ModelAttribute("userDto") UserRegistrationDto dto ,
                                  HttpServletRequest request) {





        User newUser = new User();
        newUser.setEmail(dto.getEmail());

        String Hash = passwordEncoder.encode(dto.getPassword());
        newUser.setPassword(Hash);
        newUser.setName(dto.getName());
        newUser.setLastName(dto.getLastName());
        newUser.setBalance(0.0);


        userRepository.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                newUser.getEmail(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        return "redirect:/profile";
    }
}