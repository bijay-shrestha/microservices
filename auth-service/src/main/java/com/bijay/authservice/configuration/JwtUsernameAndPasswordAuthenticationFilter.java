package com.bijay.authservice.configuration;

import com.bijay.authservice.model.UserCredentials;
import com.bijay.commonservice.security.JwtConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter
        extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authManager;

    private final JwtConfig jwtConfig;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager,
                                                      JwtConfig jwtConfig) {
        this.authManager = authManager;
        this.jwtConfig = jwtConfig;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException{

        log.info(":::: ====== ------ ATTEMPTING AUTHENTICATION ------ ====== ::::");

        try {
            UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(),
                    UserCredentials.class);

                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(creds.getUsername(),
                            creds.getPassword(),
                            Collections.emptyList());
            return authManager.authenticate(authToken);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth){

        log.info("Header :: " + jwtConfig.getHeader());
        log.info("Prefix :: " + jwtConfig.getPrefix());
        log.info("Secret :: " + jwtConfig.getSecret());
        log.info("Expiration Time :: "+ jwtConfig.getExpiration());

        log.info(":::: ====== ------ BUILDING TOKEN ------ ====== ::::");

        Long now = System.currentTimeMillis();
        String token = Jwts.builder()
                .setSubject(auth.getName())
                .claim("authorities",
                        auth.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())).setIssuedAt(new Date(now))
                .claim("user-id", 1L)
                .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))
                .signWith(SignatureAlgorithm.HS512,jwtConfig.getSecret().getBytes())
                .compact();

        log.info(":::: ====== ++++++ {} SUCCESSFULLY AUTHENTICATED AND TOKEN ADDED THE HEADER  ++++++ ====== ::::", auth.getName());
        response.addHeader(jwtConfig.getHeader(),
                jwtConfig.getPrefix() + token);
    }

}

