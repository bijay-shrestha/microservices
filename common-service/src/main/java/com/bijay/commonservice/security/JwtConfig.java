package com.bijay.commonservice.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class JwtConfig {
    @Value("${security.gateway.uri:/auth/**}")
    private String Uri;

    @Value("${security.gateway.header:Authorization}")
    private String header;

    @Value("${security.gateway.prefix:Bearer}")
    private String prefix;

    @Value("${security.jwt.expiration:#{24*60*60}}")
    private int expiration;

    @Value("${security.gateway.secret:JwtSecretKey}")
    private String secret;
}
