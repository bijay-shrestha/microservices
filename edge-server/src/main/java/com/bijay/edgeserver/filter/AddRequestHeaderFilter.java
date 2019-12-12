package com.bijay.edgeserver.filter;

import com.bijay.commonservice.security.JwtConfig;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import javax.servlet.http.HttpServletRequest;

public class AddRequestHeaderFilter extends ZuulFilter {

    private final JwtConfig jwtConfig;

    public AddRequestHeaderFilter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String username = (String) request.getAttribute("username");

        context.addZuulRequestHeader("username", username);

        return null;

    }
}
