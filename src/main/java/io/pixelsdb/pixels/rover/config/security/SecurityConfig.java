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
package io.pixelsdb.pixels.rover.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.Constants;
import io.pixelsdb.pixels.rover.constant.HttpStatus;
import io.pixelsdb.pixels.rover.mapper.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hank
 * @create 2023-04-05
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig
{

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        // Configure the paths that do not require authentication.
        http.authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/signin", "/login", "/signup", "/signup_success",
                                "/css/**", "/js/**", "/ajax/**", "/images/**", "/fonts/**",
                                "/captcha/captchaImage") // the paths
                        .permitAll()
                        .anyRequest().authenticated());
        //loginPage 指定默认登陆页面。这里需要注意：在自定义登陆页面后，必须指定登录地址
        http.formLogin().loginPage("/signin").loginProcessingUrl("/login")
                // 对参数用户名、密码的参数名进行设置
                .usernameParameter("username").passwordParameter("password")
                // 认证成功处理器
                .successHandler(new JsonAuthenticationSuccessHandler())
                .failureHandler(new JsonAuthenticationFailHandler());
        http.logout().logoutUrl("logout")   //注销登录URL，默认请求方式为GET请求
                .invalidateHttpSession(true)    // 会话失效httpSession，默认true
                .clearAuthentication(true)     // 清除认证信息，默认true
                .logoutSuccessUrl("/home");   // 注销登录，成功跳回首页

        // 增加Filter, 处理验证码
        http.addFilterBefore(new Filter() {
            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                HttpServletRequest request = (HttpServletRequest) servletRequest;
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                HttpSession session = request.getSession();

                if(request.getServletPath().equals("/login")) {
                    // 如果是登录页面, 才处理验证码
                    String verifyCode = request.getParameter("validateCode");
                    String sessionVerifyCode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
                    if(verifyCode == null || !verifyCode.equals(sessionVerifyCode)) {
                        // 验证码不正确
                      /*  throw new CaptchaException();*/
                        Map<String, Object> result = new HashMap<>();
                        result.put("msg", "Verification code error");
                        result.put("code", HttpStatus.ERROR);
                        response.setContentType("application/json;charset=UTF-8");
                        String jsonData = new ObjectMapper().writeValueAsString(result);
                        response.getWriter().write(jsonData);
                        return;
                    }
                }
                // 让请求继续向下执行
                filterChain.doFilter(request, response);
            }
        }, UsernamePasswordAuthenticationFilter.class);

        http.csrf().disable();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception
    {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository)
    {
        return new PixelsUserDetailsService(userRepository);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserRepository userRepository)
    {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService(userRepository));
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
}
