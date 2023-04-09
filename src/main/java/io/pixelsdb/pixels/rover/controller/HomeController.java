package io.pixelsdb.pixels.rover.controller;

import io.pixelsdb.pixels.rover.mapper.UserRepository;
import io.pixelsdb.pixels.rover.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

@Controller
public class HomeController {

    private final UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/home")
    public String homeGet(Authentication authentication, Model model)
    {
        requireNonNull(authentication, "authentication is null");
        checkArgument(authentication.isAuthenticated(), "user is not authenticated");
        String email = authentication.getName();
        User user = this.userRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "home";
    }

    @PostMapping("/home")
    public String homePost(Authentication authentication, Model model)
    {
        requireNonNull(authentication, "authentication is null");
        checkArgument(authentication.isAuthenticated(), "user is not authenticated");
        String email = authentication.getName();
        User user = this.userRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "home";
    }
}
