package com.lty.www.security2_form.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 记录认证失败的日志
        System.out.println("Authentication failed: " + exception.getMessage());

        // 根据异常类型处理
        if (exception instanceof BadCredentialsException) {
            // 处理用户名/密码错误
            request.setAttribute("error", "Invalid username or password");
        } else if (exception instanceof InternalAuthenticationServiceException) {
            // 处理认证服务异常
            request.setAttribute("error", "Authentication service error");
        } else {
            // 其他类型的认证失败
            request.setAttribute("error", "Authentication failed");
        }

        // 重定向回登录页面，并显示错误信息
        request.getRequestDispatcher("/login?error=true").forward(request, response);
    }
}
