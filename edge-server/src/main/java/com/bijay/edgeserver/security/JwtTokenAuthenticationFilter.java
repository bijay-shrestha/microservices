package com.bijay.edgeserver.security;

import com.bijay.commonservice.security.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;

    public JwtTokenAuthenticationFilter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        log.info(":::: ====== ------ INSIDE ZUUL GATEWAY SERVER  ------ ====== ::::");

        log.info("Header :: " + jwtConfig.getHeader());
        log.info("Prefix :: " + jwtConfig.getPrefix());
        log.info("Secret :: " + jwtConfig.getSecret());
        log.info("Expiration Time :: " + jwtConfig.getExpiration());

        // [1] GET THE AUTHENTICATION HEADER. TOKENS ARE SUPPOSED TO BE PASSED IN THE AUTHENTICATION HEADER
        String header = request.getHeader(jwtConfig.getHeader());

        // [2] VALIDATE THE HEADER AND CHECK THE PREFIX
        if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
            chain.doFilter(request, response); //If not valid, go to the next filter.
            return;
        }

        //IF THERE IS NO TOKEN PROVIDED AND HENCE THE USER WON'T BE AUTHORIZED.
        //IT'S OKAY. MAYBE THE USER ACCESSING A PUBLIC PATH OR ASKING FOR A TOKEN.

        //ALL SECURED PATHS THAT NEEDS A TOKEN ARE ALREADY DEFINED AND SECURED IN CONFIG CLASS.
        //AND IF USER TRIED TO ACCESS WITHOUT ACCESS TOKEN,
        //THEN HE WON'T BE AUTHENTICATED AND AN EXCEPTION WILL BE THROWN.

        // [3] GET THE TOKEN
        String token = header.replace(jwtConfig.getPrefix(), "");

        try { //EXCEPTION MIGHT BE THROWN IN CREATING THE CLAIMS IF FOR EXAMPLE THE TOKEN IS EXPIRED

            // [4] VALIDATE THE TOKEN
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSecret().getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            Object userId = claims.get("user-id");

            if (username != null) {
                @SuppressWarnings("unchecked")
                List<String> authorities = (List<String>) claims.get("authorities");

                // [5] CREATE AUTH OBJECT.
                // UsernamePasswordAuthenticationToken: A BUILT-IN OBJECT, USED BY SPRING TO REPRESENT
                // THE CURRENT AUTHENTICATED/ BEING AUTHENTICATED USER.

                // IT NEEDS A LIST OF AUTHORITIES, WHICH HAS TYPE OF GrantAuthority INTERFACE, WHERE
                // SimpleGrantedAuthority IS AN IMPLEMENTATION OF THAT INTERFACE
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null, authorities.stream().map(
                                SimpleGrantedAuthority::new
                        ).collect(Collectors.toList()));

                log.info("======== --------- +++++++ User with id {} successfully authenticated! +++++++++ ---------- =========", userId);

                // [6] AUTHENTICATE THE USER
                // NOW, USER IS AUTHENTICATED
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info(String.format("%s REQUEST TO %s", request.getMethod(), request.getRequestURI().toString())
                        + " " +request.getHeader(jwtConfig.getHeader()));

                request.setAttribute("username", username);
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            // IN CASE OF FAILURE. MAKE SURE IT'S CLEAR; SO GUARANTEE USER WON'T BE AUTHENTICATED.
            SecurityContextHolder.clearContext();
        }

        // GO TO THE NEXT FILTER IN THE FILTER CHAIN
        chain.doFilter(request, response);
    }
}
