package com.oriente.landing.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsService {

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    private final PasswordEncoder passwordEncoder;

    private String encodedPassword;

    @PostConstruct
    private void init() {
        this.encodedPassword = passwordEncoder.encode(adminPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (adminUsername.equals(username)) {
            return User.builder()
                    .username(adminUsername)
                    .password(encodedPassword)
                    .roles("ADMIN")
                    .build();
        }
        throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }
}
