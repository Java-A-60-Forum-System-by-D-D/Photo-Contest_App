package com.example.demo.repositories;

import com.example.demo.models.Category;
import com.example.demo.models.Contest;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.User;
import com.example.demo.models.dto.ContestViewDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestRepository extends JpaRepository<Contest,Long> {
    Optional<Contest> findByTitle(String title);
    @Query("Select c from Contest c where c.phase = 'PHASE_1'")
    List<Contest> findAllByPhase_Phase1();
//    @Query("Select c from Contest c where c.phase = 'NOT_STARTED'")
//    List<Contest> findAllByPhase_NotStarted();

    @Query("SELECT c FROM Contest c JOIN c.participants p on p.id=:userId")
    List<Contest> findAllParticipated(@Param("userId") long userId);
    @Query("Select c from Contest c join c.participants p on p.id = :userId where c.phase = 'FINISHED'")
    List<Contest> findAllFinished(@Param("userId")long userId);


}
