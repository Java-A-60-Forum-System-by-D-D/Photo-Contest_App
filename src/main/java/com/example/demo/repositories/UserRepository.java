package com.example.demo.repositories;

import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.models.filtering.UserFilterOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    List<User> findUsersByLastName(String lastName);

    Optional<User> findByUsername(String username);

    Optional<User> findUsersByEmail(String email);

    List<User> findUsersByRole(UserRole role);



    Optional<User> findUserByUsername(String username);
}