package com.example.demo.models;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

    ORGANIZER, JUNKIE, ENTHUSIAST, MASTER, DICTATOR;


    @Override
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
