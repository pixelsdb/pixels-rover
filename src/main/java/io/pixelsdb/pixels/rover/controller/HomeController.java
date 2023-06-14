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
