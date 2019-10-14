package com.bijay.zuulserver.config;

import com.bijay.commonservice.security.JwtConfig;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddCustomHeaderFilter extends ZuulFilter {
    private final JwtConfig jwtConfig;

    public AddCustomHeaderFilter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * The method filterOrder() returns an int describing the
     * order that the Filter should run in relative to others.
     * @return int
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * The method shouldFilter() returns a boolean
     * indicating if the Filter should run or not.
     * @return boolean
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        String username = (String) context.getRequest().getAttribute(jwtConfig.getUserHeaderParam());

        log.info("+++++++ ^^^^^^ ------ ADDING ZUUL HEADER WITH ID {} ------ ^^^^^^^ +++++++", username);

        context.addZuulRequestHeader(jwtConfig.getUserHeaderParam(), username);
        return null;
    }
}
