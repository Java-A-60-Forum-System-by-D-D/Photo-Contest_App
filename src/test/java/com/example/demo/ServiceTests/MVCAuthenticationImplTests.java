package com.example.demo.ServiceTests;

import com.example.demo.exceptions.AuthorizationUserException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.AuthUser;
import com.example.demo.models.User;
import com.example.demo.models.dto.LoginUserDto;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.impl.MVCAuthenticationServiceImpl;
import com.example.demo.services.impl.PhotoContestUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class MVCAuthenticationImplTests {


    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhotoContestUserDetails photoContestUserDetails;

    @InjectMocks
    private MVCAuthenticationServiceImpl mvcAuthenticationService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginUserReturnsLoginUserDtoWhenCredentialsAreValid() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        when(userRepository.findUsersByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(request.getSession()).thenReturn(session);

        LoginUserDto result = mvcAuthenticationService.loginUser("test@example.com", "password", request, response);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("password", result.getPassword());
    }

    @Test
    void loginUserThrowsAuthorizationUserExceptionWhenCredentialsAreInvalid() {
        when(userRepository.findUsersByEmail("test@example.com")).thenReturn(Optional.of(new User()));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new AuthorizationUserException("Invalid email or password"));

        assertThrows(AuthorizationUserException.class, () -> mvcAuthenticationService.loginUser("test@example.com", "wrongpassword", request, response));
    }

    @Test
    void loginUserThrowsEntityNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findUsersByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> mvcAuthenticationService.loginUser("nonexistent@example.com", "password", request, response));
    }

    @Test
    void logoutUserInvalidatesSessionAndClearsAuthentication() {
        when(request.getSession(false)).thenReturn(session);

        mvcAuthenticationService.logoutUser(request, response);

        verify(session).invalidate();
        verify(response).addCookie(any(Cookie.class));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void logoutUserDoesNothingWhenSessionIsNull() {
        when(request.getSession(false)).thenReturn(null);

        mvcAuthenticationService.logoutUser(request, response);

        verify(response).addCookie(any(Cookie.class));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }


    @Test
    void findByEmailReturnsEmptyOptionalWhenEmailNotFound() {
        Optional<AuthUser> result = mvcAuthenticationService.findByEmail("nonexistent@example.com");
        assertTrue(result.isEmpty());
    }

    @Test
    void findByUsernameReturnsEmptyOptionalWhenUsernameNotFound() {
        Optional<AuthUser> result = mvcAuthenticationService.findByUsername("nonexistentUser");
        assertTrue(result.isEmpty());
    }

    @Test
    void saveAuthUserReturnsNull() {
        AuthUser authUser = new AuthUser();
        AuthUser result = mvcAuthenticationService.saveAuthUser(authUser);
        assertNull(result);
    }

}
