/*
 * Copyright 2023 PixelsDB.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pixelsdb.pixels.rover.controller;

import io.pixelsdb.pixels.rover.model.User;
import io.pixelsdb.pixels.rover.model.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;

import static com.google.common.base.Preconditions.checkArgument;
import static io.pixelsdb.pixels.rover.controller.WebMessage.*;
import static java.util.Objects.requireNonNull;

/**
 * Created at: 4/1/23
 * Author: hank
 */
@Controller
public class WebController
{
    private final UserRepository userRepository;

    WebController(UserRepository userRepository)
    {
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

    @GetMapping("/signin") // post to "/signin" is processed by the SecurityFilterChain
    public String signForm(Authentication authentication)
    {
        if (authentication != null && authentication.isAuthenticated())
        {
            return "redirect:/home";
        }
        return "signin";
    }

    @GetMapping("/signup")
    public String signupForm(Model model)
    {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(User user, Model model)
    {
        System.out.println(user.toString());
        User existUser = userRepository.findByEmail(user.getEmail());
        if (existUser != null)
        {
            model.addAttribute("msg", USER_EXISTS);
            return "signup";
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);
        return "redirect:/signup_success"; // redirect uses get method
    }

    @GetMapping("/signup_success")
    public String signupSuccess(Model model)
    {
        model.addAttribute("msg", SIGNUP_SUCCESS);
        return "signin";
    }

    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response,
                             Authentication authentication, Model model)
    {
        if (authentication != null && authentication.isAuthenticated())
        {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            model.addAttribute("msg", LOGOUT_SUCCESS);
        }
        return "signin";
    }
}
