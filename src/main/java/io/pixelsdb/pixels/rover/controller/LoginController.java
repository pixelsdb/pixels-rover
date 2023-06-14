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

import io.pixelsdb.pixels.rover.config.common.BaseController;
import io.pixelsdb.pixels.rover.mapper.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static io.pixelsdb.pixels.rover.constant.WebMessage.LOGOUT_SUCCESS;

/**
 * @author hank
 * @create 2023-04-01
 */
@Controller
public class LoginController extends BaseController
{
    private final UserRepository userRepository;

    LoginController(UserRepository userRepository)
    {
        this.userRepository = userRepository;
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
