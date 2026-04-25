package com.auction.app.controlers;


import com.auction.app.entities.User;
import com.auction.app.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.auction.app.dao.UserRegistrationDao;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class RegistrationController {

    private final UserRepository userRepository;


    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @GetMapping("/register")
    public String showForm(Model model) {
        // We pass an empty DTO to the form to bind data to it
        model.addAttribute("userDto", new UserRegistrationDao());
        return "register_page";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("userDto") UserRegistrationDao dao) {





        User newUser = new User();
        newUser.setEmail(dao.getEmail());
        newUser.setPassword(dao.getPassword());
        newUser.setName(dao.getName());
        newUser.setLastName(dao.getLastName());
        newUser.setBalance(0.0);


        userRepository.save(newUser);

        return "redirect:/login?registered=true";
    }
}