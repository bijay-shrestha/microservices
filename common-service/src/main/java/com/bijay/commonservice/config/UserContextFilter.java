package com.bijay.commonservice.config;

import com.bijay.commonservice.security.JwtConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Slf4j
public class UserContextFilter implements Filter {

    private final JwtConfig jwtConfig;

    public UserContextFilter(@Lazy JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        log.info("======= -------- INSIDE USER CONTEXT FILTER ------- ========");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        System.out.println("I am entering the licensing service id with auth token ****: " +
                httpServletRequest.getHeader(jwtConfig.getUserHeaderParam()));

        UserContextHolder.getContext().setUsername(
                httpServletRequest.getHeader(jwtConfig.getUserHeaderParam())
        );
        chain.doFilter(httpServletRequest, response);
    }

    @Bean
    public JwtConfig jwtConfig(){
        return new JwtConfig();
    }
}