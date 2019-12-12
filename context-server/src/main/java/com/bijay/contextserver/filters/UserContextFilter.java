package com.bijay.contextserver.filters;

import com.bijay.commonservice.security.JwtConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class UserContextFilter implements Filter {

    private final JwtConfig jwtConfig;

    public UserContextFilter(@Lazy JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        System.out.println("I am entering the licensing service id with auth token ****: " + httpServletRequest.getHeader("username"));
        UserContextHolder.getContext().setUsername(httpServletRequest.getHeader("username"));

        filterChain.doFilter(httpServletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }
}