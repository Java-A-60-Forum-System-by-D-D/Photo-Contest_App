package com.example.demo.models;


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

    private String username;
    private String password;
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthUser authUser = (AuthUser) o;
        return id == authUser.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
