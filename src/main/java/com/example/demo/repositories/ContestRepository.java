package com.example.demo.repositories;

import com.example.demo.models.Category;
import com.example.demo.models.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends JpaRepository<Contest,Long> {
}
