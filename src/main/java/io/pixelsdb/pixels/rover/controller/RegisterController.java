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

import com.google.code.kaptcha.Constants;
import io.pixelsdb.pixels.rover.config.common.BaseController;
import io.pixelsdb.pixels.rover.mapper.UserRepository;
import io.pixelsdb.pixels.rover.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;

import static io.pixelsdb.pixels.rover.constant.WebMessage.SIGNUP_SUCCESS;
import static io.pixelsdb.pixels.rover.constant.WebMessage.USER_EXISTS;
import static io.pixelsdb.pixels.rover.constant.WebMessage.CAPTCHA_EXISTS;

/**
 * Register
 * @author hank
 */
@Controller
public class RegisterController extends BaseController
{

    private final UserRepository userRepository;

    public RegisterController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String signupForm(Model model)
    {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(HttpServletRequest request, HttpServletResponse response, User user, Model model)
    {
        HttpSession session = request.getSession();
        String verifyCode = request.getParameter("validateCode");
        System.out.println(verifyCode);
        String sessionVerifyCode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if(verifyCode == null || !verifyCode.equals(sessionVerifyCode)) {
            model.addAttribute("msg", CAPTCHA_EXISTS);
            return "signup";
        }
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
}
