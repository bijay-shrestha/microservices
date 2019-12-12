package com.bijay.edgeserver.security;

import com.bijay.commonservice.security.JwtConfig;
import com.bijay.edgeserver.filter.AddRequestHeaderFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity // ENABLE SECURITY CONFIG. THIS ANNOTATION DENOTES CONFIG FOR SPRING SECURITY.
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfig jwtConfig;

    public SecurityTokenConfig(@Lazy JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                // MAKE SURE WE USE STATELESS SESSION; SESSION WON'T BE USED TO STORE USER'S STATE.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // HANDLE AN AUTHORIZED ATTEMPTS
                .exceptionHandling().authenticationEntryPoint(
                (req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                // ADD A FILTER TO VALIDATE THE TOKENS WITH EVERY REQUEST
                .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig),
                        UsernamePasswordAuthenticationFilter.class)
                // AUTHORIZATION REQUEST CONFIG
                .authorizeRequests()
                // ALLOW ALL WHO ARE ACCESSING 'auth' SERVICE
                .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
                // MUST BE ADMIN IF TRYING TO ACCESS ADMIN AREA (AUTHENTICATION IS ALSO REQUIRED HERE)
                .antMatchers("/gallery" + "/admin/**").hasRole("ADMIN")
                // ANY OTHER REQUEST MUST BE AUTHENTICATED
                .anyRequest().authenticated();

    }

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }

    @Bean
    public AddRequestHeaderFilter addRequestHeaderFilter(JwtConfig jwtConfig) {
        return new AddRequestHeaderFilter(jwtConfig);
    }
}
