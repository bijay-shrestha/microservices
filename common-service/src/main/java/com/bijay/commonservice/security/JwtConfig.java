package com.bijay.commonservice.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class JwtConfig {
    @Value("${security.jwt.uri:/login/**}")
    private String Uri;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.expiration:#{24*60*60}}")
    private int expiration;

    @Value("${security.jwt.secret:NovemberRainGuns&Roses}")
    private String secret;

    @Value("${security.jwt.userHeaderParam:X-User-Header}")
    private String userHeaderParam;
}
