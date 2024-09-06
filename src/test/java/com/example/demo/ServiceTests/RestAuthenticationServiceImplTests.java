package com.example.demo.ServiceTests;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.PasswordMismatchException;
import com.example.demo.models.AuthUser;
import com.example.demo.models.dto.LoginUserDto;
import com.example.demo.repositories.AuthUserRepository;
import com.example.demo.services.TokenService;
import com.example.demo.services.impl.PhotoContestUserDetails;
import com.example.demo.services.impl.RestAuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestAuthenticationServiceImplTests {


    @Mock
    private AuthUserRepository authUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private PhotoContestUserDetails photoContestUserDetails;

    @InjectMocks
    private RestAuthenticationServiceImpl restAuthenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginUserReturnsLoginUserDtoWhenCredentialsAreValid() {
        AuthUser user = new AuthUser();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("test@example.com");
        loginUserDto.setPassword("password");

        when(authUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(tokenService.generateJwt(any(Authentication.class))).thenReturn("token");

        LoginUserDto result = restAuthenticationService.loginUser(loginUserDto);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("token", result.getToken());
    }

    @Test
    void loginUserThrowsEntityNotFoundExceptionWhenUserDoesNotExist() {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("nonexistent@example.com");
        loginUserDto.setPassword("password");

        when(authUserRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> restAuthenticationService.loginUser(loginUserDto));
    }

    @Test
    void loginUserThrowsPasswordMismatchExceptionWhenPasswordIsIncorrect() {
        AuthUser user = new AuthUser();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("test@example.com");
        loginUserDto.setPassword("wrongpassword");

        when(authUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        assertThrows(PasswordMismatchException.class, () -> restAuthenticationService.loginUser(loginUserDto));
    }

    @Test
    void findByEmailReturnsAuthUserWhenExists() {
        AuthUser user = new AuthUser();
        user.setEmail("test@example.com");

        when(authUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<AuthUser> result = restAuthenticationService.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void findByEmailReturnsEmptyWhenUserDoesNotExist() {
        when(authUserRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<AuthUser> result = restAuthenticationService.findByEmail("nonexistent@example.com");

        assertFalse(result.isPresent());
    }

    @Test
    void findByUsernameReturnsAuthUserWhenExists() {
        AuthUser user = new AuthUser();
        user.setUsername("testuser");

        when(authUserRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<AuthUser> result = restAuthenticationService.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void findByUsernameReturnsEmptyWhenUserDoesNotExist() {
        when(authUserRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        Optional<AuthUser> result = restAuthenticationService.findByUsername("nonexistentuser");

        assertFalse(result.isPresent());
    }

    @Test
    void saveAuthUserSavesAndReturnsAuthUser() {
        AuthUser user = new AuthUser();
        user.setEmail("test@example.com");

        when(authUserRepository.save(user)).thenReturn(user);

        AuthUser result = restAuthenticationService.saveAuthUser(user);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }
}
