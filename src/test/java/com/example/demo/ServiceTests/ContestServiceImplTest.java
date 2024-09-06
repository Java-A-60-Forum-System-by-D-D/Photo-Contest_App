package com.example.demo.ServiceTests;

import com.example.demo.exceptions.AuthorizationUserException;
import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Contest;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.repositories.ContestRepository;
import com.example.demo.services.UserService;
import com.example.demo.services.impl.ContestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContestServiceImplTest {

    @Mock
    private ContestRepository contestRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ContestServiceImpl contestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getContestByIdReturnsContestWhenExists() {
        Contest contest = new Contest();
        when(contestRepository.findById(1L)).thenReturn(Optional.of(contest));

        Contest result = contestService.getContestById(1L);

        assertNotNull(result);
    }

    @Test
    void getContestByIdThrowsEntityNotFoundExceptionWhenNotExists() {
        when(contestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> contestService.getContestById(1L));
    }

    @Test
    void createContestSavesAndReturnsContestWhenUserIsOrganizer() {
        User user = new User();
        user.setJurorContests(new ArrayList<>());
        user.setRole(UserRole.ORGANIZER);
        Contest contest = new Contest();
        contest.setTitle("New Contest");

        when(contestRepository.findByTitle("New Contest")).thenReturn(Optional.empty());
        when(contestRepository.save(contest)).thenReturn(contest);
        when(userService.findUsersByRoleOrganizer("ORGANIZER")).thenReturn(List.of(user));

        Contest result = contestService.createContest(contest, user);

        assertNotNull(result);
        verify(contestRepository).save(contest);
    }

    @Test
    void createContestThrowsAuthorizationUserExceptionWhenUserIsNotOrganizer() {
        User user = new User();
        user.setRole(UserRole.JUNKIE);
        Contest contest = new Contest();

        assertThrows(AuthorizationUserException.class, () -> contestService.createContest(contest, user));
    }

    @Test
    void createContestThrowsEntityDuplicateExceptionWhenTitleExists() {
        User user = new User();
        user.setRole(UserRole.ORGANIZER);
        Contest contest = new Contest();
        contest.setTitle("Existing Contest");

        when(contestRepository.findByTitle("Existing Contest")).thenReturn(Optional.of(contest));

        assertThrows(EntityDuplicateException.class, () -> contestService.createContest(contest, user));
    }

    @Test
    void addJuryAddsJuryWhenUserIsOrganizerAndJuryIsMaster() {
        User user = new User();
        user.setOrganizedContests(new ArrayList<>());
        user.setRole(UserRole.ORGANIZER);
        Contest contest = new Contest();
        user.getOrganizedContests().add(contest);
        User jury = new User();
        jury.setRole(UserRole.MASTER);

        when(contestRepository.findById(1L)).thenReturn(Optional.of(contest));
        when(userService.save(jury)).thenReturn(jury);

        Contest result = contestService.addJury(1L, jury, user);

        assertNotNull(result);
        verify(userService).save(jury);
    }

    @Test
    void addJuryThrowsAuthorizationUserExceptionWhenUserIsNotOrganizer() {
        User user = new User();
        user.setRole(UserRole.JUNKIE);
        Contest contest = new Contest();
        User jury = new User();
        jury.setRole(UserRole.MASTER);

        when(contestRepository.findById(1L)).thenReturn(Optional.of(contest));

        assertThrows(AuthorizationUserException.class, () -> contestService.addJury(1L, jury, user));
    }

    @Test
    void addJuryThrowsEntityDuplicateExceptionWhenJuryAlreadyAdded() {
        User user = new User();
        user.setOrganizedContests(new ArrayList<>());
        user.setRole(UserRole.ORGANIZER);
        Contest contest = new Contest();
        user.getOrganizedContests().add(contest);
        User jury = new User();
        jury.setRole(UserRole.MASTER);
        jury.setJurorContests(new ArrayList<>());
        jury.getJurorContests().add(contest);

        when(contestRepository.findById(1L)).thenReturn(Optional.of(contest));

        assertThrows(EntityDuplicateException.class, () -> contestService.addJury(1L, jury, user));
    }

//    @Test
//    void inviteUserToContestInvitesUserWhenUserIsOrganizer() {
//        User user = new User();
//        user.setOrganizedContests(new ArrayList<>());
//        user.setRole(UserRole.ORGANIZER);
//        Contest contest = new Contest();
//        user.getOrganizedContests().add(contest);
//        User userToInvite = new User();
//        userToInvite.setRole(UserRole.JUNKIE);
//
//        when(contestRepository.findById(1L)).thenReturn(Optional.of(contest));
//        when(userService.saveUser(userToInvite)).thenReturn(userToInvite);
//
//        Contest result = contestService.inviteUserToContest(1L, userToInvite, user);
//
//        assertNotNull(result);
//        verify(userService).saveUser(userToInvite);
//    }

    @Test
    void inviteUserToContestThrowsAuthorizationUserExceptionWhenUserIsNotOrganizer() {
        User user = new User();
        user.setRole(UserRole.JUNKIE);
        Contest contest = new Contest();
        User userToInvite = new User();
        userToInvite.setRole(UserRole.JUNKIE);

        when(contestRepository.findById(1L)).thenReturn(Optional.of(contest));

        assertThrows(AuthorizationUserException.class, () -> contestService.inviteUserToContest(1L, userToInvite, user));
    }

//    @Test
//    void inviteUserToContestThrowsEntityDuplicateExceptionWhenUserAlreadyInvited() {
//        User user = new User();
//        user.setParticipatedContests(new ArrayList<>());
//        user.setRole(UserRole.ORGANIZER);
//        Contest contest = new Contest();
//        User userToInvite = new User();
//        userToInvite.setRole(UserRole.JUNKIE);
//        userToInvite.getParticipatedContests().add(contest);
//
//        when(contestRepository.findById(1L)).thenReturn(Optional.of(contest));
//
//        assertThrows(EntityDuplicateException.class, () -> contestService.inviteUserToContest(1L, userToInvite, user));
//    }

//    @Test
//    void calculateFinalContestPointsReturnsTop3UsersWithScores() {
//        List<PhotoSubmission> submissions = List.of(
//                new PhotoSubmission(1, 50),
//                new PhotoSubmission(2, 40),
//                new PhotoSubmission(3, 30)
//        );
//        List<User> users = List.of(new User(), new User(), new User());
//
//        TreeMap<Integer, List<User>> result = contestService.calculateFinalContestPoints(submissions, users);
//
//        assertEquals(3, result.size());
//    }

    @Test
    void calculateFinalContestPointsReturnsEmptyWhenNoSubmissions() {
        List<PhotoSubmission> submissions = List.of();
        List<User> users = List.of();

        TreeMap<Integer, List<User>> result = contestService.calculateFinalContestPoints(submissions, users);

        assertTrue(result.isEmpty());
    }
}
