package com.bijay.jwt.configuration;

import com.bijay.jwt.security.JwtConfig;
import com.bijay.jwt.security.JwtTokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtConfig jwtConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
      http.csrf().disable()
              .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              .and()
              .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
              .authorizeRequests()
              .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
              .antMatchers("/gallery" + "/admin/**").hasRole("ADMIN")
              .anyRequest().authenticated();

    }

    @Bean
    public JwtConfig jwtConfig(){
        return new JwtConfig();
    }
}
