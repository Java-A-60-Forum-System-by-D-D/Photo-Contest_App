package com.example.demo.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "auth_users")
public class AuthUser extends BaseEntity {

    @Column(name = "username",unique = true)
    private String username;
    @Column(name = "password",unique = true)
    private String password;
    @Column(name = "email",unique = true)
    private String email;


}
