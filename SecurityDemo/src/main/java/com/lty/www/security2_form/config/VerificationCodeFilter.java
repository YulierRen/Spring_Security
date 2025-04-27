package com.lty.www.security2_form.config;

import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class VerificationCodeFilter extends OncePerRequestFilter {

    private AuthenticationFailureHandler authenticationFailureHandler = new MyAuthenticationFailureHandler();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!"/auth/form".equals(request.getRequestURI())){
            filterChain.doFilter(request, response);
        }else{
            try{
                verificationCode(request);
                filterChain.doFilter(request, response);
            }catch (VerificationCodeException e){
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
            }
        }
    }

    public void verificationCode(HttpServletRequest request) throws VerificationCodeException{
        String verificationCode = request.getParameter("captcha");
        HttpSession session = request.getSession();
        String savedCode = (String) session.getAttribute("captcha");
        if(!StringUtils.isEmpty(savedCode)){
            session.removeAttribute("captcha");
        }
        if(StringUtils.isEmpty(verificationCode)||StringUtils.isEmpty(savedCode)||!verificationCode.equals(savedCode)){
            throw new VerificationCodeException("验证码错误");
        }
    }
}
