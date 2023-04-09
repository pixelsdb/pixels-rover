package io.pixelsdb.pixels.rover.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pixelsdb.pixels.rover.constant.HttpStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonAuthenticationFailHandler implements AuthenticationFailureHandler
{

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException
    {
        Map<String, Object> result = new HashMap<>();
        result.put("msg", "账号或密码错误！！！");
        result.put("code", HttpStatus.ERROR);
        result.put("exception", exception.getMessage());
        response.setContentType("application/json;charset=UTF-8");
        String jsonData = new ObjectMapper().writeValueAsString(result);
        response.getWriter().write(jsonData);
    }

}
