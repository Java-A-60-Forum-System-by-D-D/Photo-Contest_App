package com.example.demo.repositories;

import com.example.demo.models.Category;
import com.example.demo.models.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContestRepository extends JpaRepository<Contest,Long> {
    Optional<Contest> findByTitle(String title);
}
