package com.bijay.authservice.security;

import com.bijay.authservice.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final List<Admin> admins = Arrays.asList(
                new Admin(1L, "bijay", encoder.encode("1234"), "USER"),
                new Admin(2L, "bishow", encoder.encode("12345"), "ADMIN")
        );

        for (Admin admin : admins) {
            if (admin.getUsername().equals(username)) {
                List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                        .commaSeparatedStringToAuthorityList("ROLE_" + admin.getRole());
                return new User(admin.getUsername(), admin.getPassword(), grantedAuthorities);
            }

        }
        throw new UsernameNotFoundException("Username: " + username + " not found.");
    }
}
