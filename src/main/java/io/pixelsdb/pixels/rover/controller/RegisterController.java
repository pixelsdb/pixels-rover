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
 * 注册
 * @author hank
 */
@Controller
public class RegisterController extends BaseController {

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
    public String signupSubmit(HttpServletRequest request, HttpServletResponse response,User user, Model model)
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
