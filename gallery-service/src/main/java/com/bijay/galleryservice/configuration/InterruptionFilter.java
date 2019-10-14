package com.bijay.galleryservice.configuration;

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
public class InterruptionFilter implements Filter {

    private final JwtConfig jwtConfig;

    public InterruptionFilter(@Lazy JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        log.info("======= -------- INSIDE INTERRUPTION FILTER ------- ========");

        HttpServletRequest req = (HttpServletRequest)request;
        System.out.println(" REQUEST ------  " + ((HttpServletRequest) request)
                .getHeader(jwtConfig.getUserHeaderParam()));

        if("yes".equalsIgnoreCase(req.getParameter("interrupt")))
            response.getWriter().write("Sorry, interrupted");
        else
            chain.doFilter(request, response);

        System.out.println("Returning from filter/servlet chain");
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }

    @Bean
    public JwtConfig jwtConfig(){
        return new JwtConfig();
    }
}