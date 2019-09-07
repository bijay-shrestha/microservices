package com.bijay.galleryservice.configuration;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class InterruptionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;

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
}