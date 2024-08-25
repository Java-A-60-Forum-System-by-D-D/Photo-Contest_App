package com.example.demo.repositories;

import com.example.demo.models.AuthUser;
import com.example.demo.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserRepository  extends JpaRepository<AuthUser,Long> {
}
