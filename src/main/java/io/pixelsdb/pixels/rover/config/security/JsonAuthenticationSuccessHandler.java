package io.pixelsdb.pixels.rover.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pixelsdb.pixels.rover.config.common.AjaxResult;
import io.pixelsdb.pixels.rover.constant.HttpStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        Map<String, Object> result = new HashMap<>();
        result.put("msg", "登录成功");
        result.put("code", HttpStatus.SUCCESS);
        result.put("authentication", authentication);
        response.setContentType("application/json;charset=UTF-8");
        String jsonData = new ObjectMapper().writeValueAsString(result);
        response.getWriter().write(jsonData);
    }
}
