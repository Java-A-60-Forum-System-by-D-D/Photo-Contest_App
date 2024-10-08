package com.example.demo.services.impl;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.PasswordMismatchException;
import com.example.demo.models.AuthUser;
import com.example.demo.models.dto.LoginUserDto;
import com.example.demo.repositories.AuthUserRepository;
import com.example.demo.services.TokenService;
import com.example.demo.services.RestAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestAuthenticationServiceImpl implements RestAuthenticationService {
    public static final String USER_DOES_NOT_EXIST = "User does not exist";
    public static final String PASSWORD_MISMATCH = "Password mismatch";



    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PhotoContestUserDetails photoContestUserDetails;

    @Autowired
    public RestAuthenticationServiceImpl(AuthUserRepository authUserRepository,
                                         PasswordEncoder passwordEncoder,
                                         @Qualifier("restAuthenticationManager") AuthenticationManager authenticationManager,
                                         TokenService tokenService,
                                         PhotoContestUserDetails photoContestUserDetails) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.photoContestUserDetails = photoContestUserDetails;
    }

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return authUserRepository.findByEmail(email);
    }

    @Override
    public Optional<AuthUser> findByUsername(String username) {
        return authUserRepository.findByUsername(username);
    }

    @Override
    public LoginUserDto loginUser(LoginUserDto loginUserDto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword())
        );
        String token = tokenService.generateJwt(auth);

        Optional<AuthUser> authUser = findByEmail(loginUserDto.getEmail());
        if (authUser.isEmpty()) {
            throw new EntityNotFoundException(USER_DOES_NOT_EXIST);
        }
        AuthUser user = authUser.get();
        if (!passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword())) {
            throw new PasswordMismatchException(PASSWORD_MISMATCH);
        }

        loginUserDto.setToken(token);
        photoContestUserDetails.loadUserByUsername(loginUserDto.getEmail());
        return loginUserDto;
    }

    @Override
    public AuthUser saveAuthUser(AuthUser authUser) {
        return authUserRepository.save(authUser);
    }



}