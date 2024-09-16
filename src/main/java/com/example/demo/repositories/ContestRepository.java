package com.example.demo.repositories;

import com.example.demo.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestRepository extends JpaRepository<Contest,Long>, JpaSpecificationExecutor<Contest> {
    Optional<Contest> findByTitle(String title);
    @Query("Select c from Contest c where c.phase = 'PHASE_1'")
    List<Contest> findAllByPhase_Phase1();
//    @Query("Select c from Contest c where c.phase = 'NOT_STARTED'")
//    List<Contest> findAllByPhase_NotStarted();
@Query("Select c from Contest c where c.phase = 'PHASE_1' and c.type = 'OPEN'")
List<Contest> findAllByPhase_Phase1_AndType_Open();

    @Query("SELECT c FROM Contest c JOIN c.participants p on p.id=:userId")
    List<Contest> findAllParticipated(@Param("userId") long userId);
    @Query("Select c from Contest c join c.participants p on p.id = :userId where c.phase = 'FINISHED'")
    List<Contest> findAllFinished(@Param("userId")long userId);

    @Query("Select c from Contest c where c.phase != 'FINISHED'")
    List<Contest> findAllUnfinishedContests();

    @Query("SELECT c FROM Contest c WHERE " +
            "(:title IS NULL OR c.title = :title) AND " +
            "(:category IS NULL OR c.category.name = :category) AND " +
            "(:type IS NULL OR c.type = :type) AND " +
            "(:phase IS NULL OR c.phase = :phase) AND " +
            "(:username IS NULL OR c.organizer.username = :username)")
    List<Contest> findAllByFilterOptions(@Param("title") String title,
                                         @Param("category") String category,
                                         @Param("type") Type type,
                                         @Param("phase") Phase phase,
                                         @Param("username") String username);



    @Query("SELECT c FROM Contest c WHERE c.phase = 'FINISHED' AND c.category.id = :categoryId")
    List<Contest> getFinishedContestsByCategory(@Param("categoryId") Long categoryId);
    @Query("select c from Contest c where c.phase = 'PHASE_2'")
    List<Contest> findAllByPhase_Phase2();
    @Query("select c from Contest c where c.phase = 'FINISHED'")
    List<Contest> findAllByPhase_Finished();

    List<Contest> findContestByCategory_Id(long id);

    @Query("Select c from Contest c where c.phase = 'NOT_STARTED'")
    List<Contest> findContestByPhase_NotStarted();
    @Query("Select c.photoUrl from Contest c")
    List<String> getAllByPhotos();

    @Query("Select c from Contest c join c.invitedUsers i on i.id = :id")
    List<Contest> findContestsByInvitedUsersEqualsUserId(@Param("id") long id);
    @Query("Select c from Contest c where c.category.id = :id order by size (c.submissions) desc limit 4")
    List<Contest> findContestsByCategoryId(long id);
}
