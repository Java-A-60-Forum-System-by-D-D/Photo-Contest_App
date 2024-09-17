package com.example.demo.services.impl;

import com.example.demo.exceptions.AuthorizationUserException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.AuthUser;
import com.example.demo.models.User;
import com.example.demo.models.dto.LoginUserDto;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.MVCAuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.util.Optional;

@Service
@Qualifier("mvcAuthenticationService")
public class MVCAuthenticationServiceImpl implements MVCAuthenticationService {

    public static final String USER_WITH_SUCH_EMAIL_DOESNT_EXIST = "User with such email doesnt exist";
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PhotoContestUserDetails photoContestUserDetails;

    public MVCAuthenticationServiceImpl(@Qualifier("mvcAuthenticationManager") AuthenticationManager authenticationManager,
                                        UserRepository userRepository,
                                        PhotoContestUserDetails photoContestUserDetails) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.photoContestUserDetails = photoContestUserDetails;
    }


    @Override
    public LoginUserDto loginUser(String email, String password, HttpServletRequest request, HttpServletResponse response) {
        User user = userRepository.findUsersByEmail(email).orElseThrow(() -> new EntityNotFoundException(USER_WITH_SUCH_EMAIL_DOESNT_EXIST));

        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            Cookie sessionCookie = new Cookie("SESSIONID", WebUtils.getSessionId(request));
            sessionCookie.setHttpOnly(true);
            sessionCookie.setSecure(true);
            sessionCookie.setPath("/**");
            response.addCookie(sessionCookie);
            LoginUserDto loggInUserDTO = new LoginUserDto();
            loggInUserDTO.setEmail(user.getEmail());
            loggInUserDTO.setPassword(user.getPassword());
            photoContestUserDetails.loadUserByUsername(user.getEmail());
            return loggInUserDTO;
        } catch (AuthenticationException e) {
            throw new AuthorizationUserException("Invalid email or password");
        }
    }



    @Override
    public void logoutUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        Cookie cookie = new Cookie("SESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        SecurityContextHolder.clearContext();
    }

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<AuthUser> findByUsername(String username) {
        return Optional.empty();
    }


    @Override
    public AuthUser saveAuthUser(AuthUser authUser) {
        return null;
    }
}
