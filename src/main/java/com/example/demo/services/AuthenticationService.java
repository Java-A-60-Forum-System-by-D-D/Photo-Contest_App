package com.example.demo.services;

import com.example.demo.models.AuthUser;

public interface AuthenticationService {
    AuthUser findByEmail(String email);
}
