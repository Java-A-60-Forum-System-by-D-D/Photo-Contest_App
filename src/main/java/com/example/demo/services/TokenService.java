package com.example.demo.services;

import org.springframework.security.core.Authentication;

public interface TokenService {
    String generateJwt(Authentication auth);
}
