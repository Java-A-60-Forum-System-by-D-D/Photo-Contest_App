package com.example.demo.repositories;

import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.models.filtering.UserFilterOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("Select u from users u join u.jurorContests c where c.id = :id")
    List<User> findUsersByJurorContests(@Param("id") long id);

    @Query("Select u From users u where u.role != 'ORGANIZER'")
    List<User> findParticipantUsers();
    @Query("SELECT u.email FROM AuthUser u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<String> findEmailByTerm(@Param("term") String term);

    @Query("SELECT u FROM users u WHERE " +
            "(:email IS NULL OR u.email like CONCAT('%', :email, '%')) AND " +
            "(:firstName IS NULL OR u.firstName like CONCAT('%', :firstName, '%')) AND " +
            "(:lastName IS NULL OR u.lastName like CONCAT('%', :lastName, '%')) AND " +
            "(:role IS NULL OR u.role = :role)")
    List<User> findAllByOptions(@Param("email") Optional<String> email,
                                @Param("firstName") Optional<String> firstName,
                                @Param("lastName") Optional<String> lastName,
                                @Param("role") Optional<UserRole> role);

}