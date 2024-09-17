package com.example.demo.ServiceTests;// PhotoContestUserDetailsTest.java

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.AuthUser;
import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.repositories.AuthUserRepository;
import com.example.demo.services.impl.PhotoContestUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhotoContestUserDetailsTest {

    private AuthUserRepository authUserRepository;
    private PhotoContestUserDetails photoContestUserDetails;

    @BeforeEach
    void setUp() {
        authUserRepository = mock(AuthUserRepository.class);
        photoContestUserDetails = new PhotoContestUserDetails(authUserRepository);
    }

    @Test
    void loadUserByUsernameReturnsUserDetailsWhenUserExists() {
        AuthUser authUser = new AuthUser();
        authUser.setEmail("test@example.com");
        authUser.setPassword("password");

        when(authUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(authUser));

        UserDetails userDetails = photoContestUserDetails.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    void loadUserByUsernameThrowsExceptionWhenUserNotFound() {
        when(authUserRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> photoContestUserDetails.loadUserByUsername("nonexistent@example.com"));
    }

    @Test
    void loadUserByUsernameReturnsEmptyAuthoritiesWhenUserHasNoRole() {
        AuthUser authUser = new AuthUser();
        authUser.setEmail("test@example.com");
        authUser.setPassword("password");

        when(authUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(authUser));

        UserDetails userDetails = photoContestUserDetails.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }


}