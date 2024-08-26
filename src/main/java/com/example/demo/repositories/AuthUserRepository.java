package com.example.demo.repositories;

import com.example.demo.models.AuthUser;
import com.example.demo.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository  extends JpaRepository<AuthUser,Long> {
    Optional<AuthUser> findByEmail(String email);

    Optional<AuthUser> findByUsername(String username);

}
