package com.example.demo.services.impl;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.AuthUser;
import com.example.demo.models.UserRole;
import com.example.demo.repositories.AuthUserRepository;
import com.example.demo.services.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

        List<GrantedAuthority> authorities = mapRolesToAuthorities(authUser);

        return org.springframework.security.core.userdetails.User
                .withUsername(authUser.getEmail())
                .password(authUser.getPassword())
                .authorities(authorities)
                .build();
    }

    private List<GrantedAuthority> mapRolesToAuthorities(AuthUser authUser) {

        if (authUser instanceof com.example.demo.models.User) {
            UserRole role = ((com.example.demo.models.User) authUser).getRole();
            return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
        }
        return Collections.emptyList();
    }
}
