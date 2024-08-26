package com.example.demo.services.impl;

import com.example.demo.exceptions.PasswordMismatchException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.models.AuthUser;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.dto.LoginUserDto;
import com.example.demo.repositories.AuthUserRepository;
import com.example.demo.services.AuthenticationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    public static final String USER_DOES_NOT_EXIST = "User does not exist";
    public static final String PASSWORD_MISMATCH = "Password mismatch";
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhotoContestUserDetails photoContestUserDetails;

    public AuthenticationServiceImpl(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder, PhotoContestUserDetails photoContestUserDetails) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
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
    public AuthUser loginUser(LoginUserDto loginUserDto) {

        Optional<AuthUser> authUser = findByEmail(loginUserDto.getEmail());
        if(authUser.isEmpty()){
            throw new UserNotFoundException(USER_DOES_NOT_EXIST);
        }
        AuthUser user = authUser.get();
        if (!passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword())) {
            throw new PasswordMismatchException(PASSWORD_MISMATCH);
        }
        photoContestUserDetails.loadUserByUsername(user.getEmail());
        return user;

    }

    @Override
    public AuthUser saveAuthUser(AuthUser authUser) {
       return authUserRepository.save(authUser);
    }
}
