package com.example.demo.services.impl;

import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.models.AuthUser;
import com.example.demo.repositories.AuthUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PhotoContestUserDetails implements UserDetailsService {
    private final AuthUserRepository authUserRepository;

    public PhotoContestUserDetails(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       AuthUser authUser = authUserRepository.findByEmail(email).stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
        return org.springframework.security.core.userdetails.User
                .withUsername(authUser.getEmail())
                .password(authUser.getPassword())
                .build();
    }
}
