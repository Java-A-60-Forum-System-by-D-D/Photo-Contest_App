package com.example.demo.ServiceTests;

import com.example.demo.models.*;
import com.example.demo.repositories.ContestRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.NotificationService;
import com.example.demo.services.UserService;
import com.example.demo.services.schedulers.PhaseScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PhaseSchedulerTest {

    @Mock
    private ContestRepository contestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PhaseScheduler phaseScheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void startPhase2UpdatesPhaseToPhase1WhenNotStartedAndTimeIsBeforeStartPhase1() {
        LocalDateTime now = LocalDateTime.now();
        Contest contest = new Contest();
        contest.setPhase(Phase.NOT_STARTED);
        contest.setStartPhase1(now.minusDays(1));
        contest.setStartPhase2(now.plusDays(1)); // Ensure startPhase2 is set
        contest.setStartPhase3(now.plusDays(2)); // Ensure startPhase3 is set
        List<Contest> contests = List.of(contest);
        when(contestRepository.findAllUnfinishedContests()).thenReturn(contests);

        phaseScheduler.startPhase2();

        assertEquals(Phase.PHASE_1, contest.getPhase());
        verify(notificationService).sendNotification(anyString(), eq(NotificationType.CONTEST_UPDATE), eq(contest.getOrganizer()));
        verify(contestRepository).save(contest);
    }

    @Test
    void startPhase2UpdatesPhaseToPhase2WhenNotInPhase2AndTimeIsBeforeStartPhase2() {
        LocalDateTime now = LocalDateTime.now();
        Contest contest = new Contest();
        contest.setPhase(Phase.PHASE_1);
        contest.setStartPhase1(now.minusDays(2)); // Ensure startPhase1 is set
        contest.setStartPhase2(now.minusDays(1));
        contest.setStartPhase3(now.plusDays(1)); // Ensure startPhase3 is set
        List<Contest> contests = List.of(contest);
        when(contestRepository.findAllUnfinishedContests()).thenReturn(contests);

        phaseScheduler.startPhase2();

        assertEquals(Phase.PHASE_2, contest.getPhase());
        verify(contestRepository).save(contest);
    }

    @Test
    void startPhase2UpdatesPhaseToFinishedWhenNotFinishedAndTimeIsBeforeStartPhase3() {
        LocalDateTime now = LocalDateTime.now();
        Contest contest = new Contest();
        contest.setPhase(Phase.PHASE_2);
        contest.setStartPhase1(now.minusDays(3)); // Ensure startPhase1 is set
        contest.setStartPhase2(now.minusDays(2)); // Ensure startPhase2 is set
        contest.setStartPhase3(now.minusDays(1));
        contest.setSubmissions(new ArrayList<>());
        List<Contest> contests = List.of(contest);
        when(contestRepository.findAllUnfinishedContests()).thenReturn(contests);

        phaseScheduler.startPhase2();

        assertEquals(Phase.FINISHED, contest.getPhase());
        verify(contestRepository).save(contest);
    }

    @Test
    void startPhase2DoesNotUpdatePhaseWhenAlreadyInPhase1() {
        LocalDateTime now = LocalDateTime.now();
        Contest contest = new Contest();
        contest.setPhase(Phase.PHASE_1);
        contest.setStartPhase1(now.minusDays(1));
        contest.setStartPhase2(now.plusDays(1)); // Ensure startPhase2 is set
        contest.setStartPhase3(now.plusDays(2)); // Ensure startPhase3 is set
        List<Contest> contests = List.of(contest);
        when(contestRepository.findAllUnfinishedContests()).thenReturn(contests);

        phaseScheduler.startPhase2();

        assertEquals(Phase.PHASE_1, contest.getPhase());
        verify(contestRepository, never()).save(contest);
    }

    @Test
    void startPhase2DoesNotUpdatePhaseWhenAlreadyInPhase2() {
        LocalDateTime now = LocalDateTime.now();
        Contest contest = new Contest();
        contest.setPhase(Phase.PHASE_2);
        contest.setStartPhase1(now.minusDays(2)); // Ensure startPhase1 is set
        contest.setStartPhase2(now.minusDays(1));
        contest.setStartPhase3(now.plusDays(1)); // Ensure startPhase3 is set
        List<Contest> contests = List.of(contest);
        when(contestRepository.findAllUnfinishedContests()).thenReturn(contests);

        phaseScheduler.startPhase2();

        assertEquals(Phase.PHASE_2, contest.getPhase());
        verify(contestRepository, never()).save(contest);
    }


}