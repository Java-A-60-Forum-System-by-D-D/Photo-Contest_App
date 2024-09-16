package com.example.demo.ServiceTests;

import com.example.demo.exceptions.AuthorizationUserException;
import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.*;
import com.example.demo.models.filtering.ContestFilterOptions;
import com.example.demo.repositories.ContestRepository;
import com.example.demo.services.NotificationService;
import com.example.demo.services.UserService;
import com.example.demo.services.impl.ContestServiceImpl;
import com.example.demo.services.impl.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.times;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContestServiceImplTest {

    @Mock
    private ContestRepository contestRepository;

    @Mock
    private UserService userService;

    @Mock
    private EmailService emailService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ContestServiceImpl contestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



@Test
void rankingAssignsCorrectScoresToTopThreeUsers() {
    User user1 = new User();
    User user2 = new User();
    User user3 = new User();
    User user4 = new User();
    user1.setTotalScore(0);
    user2.setTotalScore(0);
    user3.setTotalScore(0);
    user4.setTotalScore(0);

    TreeMap<Integer, List<User>> top3 = new TreeMap<>(Comparator.reverseOrder());
    top3.put(100, List.of(user1));
    top3.put(90, List.of(user2));
    top3.put(80, List.of(user3, user4));

    contestService.ranking(top3);

    assertEquals(50, user1.getTotalScore());
    assertEquals(35, user2.getTotalScore());
    assertEquals(10, user3.getTotalScore());
    assertEquals(10, user4.getTotalScore());
}

    @Test
    void rankingAssignsCorrectScoresWhenOnlyOneUser() {
        User user1 = new User();
        user1.setTotalScore(0);

        TreeMap<Integer, List<User>> top3 = new TreeMap<>(Comparator.reverseOrder());
        top3.put(100, List.of(user1));

        contestService.ranking(top3);

        assertEquals(50, user1.getTotalScore());
    }

    @Test
    void rankingAssignsCorrectScoresWhenTwoUsersWithSameScore() {
        User user1 = new User();
        User user2 = new User();
        user1.setTotalScore(0);
        user2.setTotalScore(0);

        TreeMap<Integer, List<User>> top3 = new TreeMap<>(Comparator.reverseOrder());
        top3.put(100, List.of(user1, user2));

        contestService.ranking(top3);

        assertEquals(40, user1.getTotalScore());
        assertEquals(40, user2.getTotalScore());
    }

    @Test
    void rankingAssignsCorrectScoresWhenNoUsers() {
        TreeMap<Integer, List<User>> top3 = new TreeMap<>(Comparator.reverseOrder());

        contestService.ranking(top3);

        assertTrue(top3.isEmpty());
    }



    @Test
    void getPhaseOneContestsAndTypeOpenReturnsListOfContests() {
        List<Contest> contests = List.of(new Contest(), new Contest());
        when(contestRepository.findAllByPhase_Phase1_AndType_Open()).thenReturn(contests);

        List<Contest> result = contestService.getPhaseOneContestsAndTypeOpen();

        assertEquals(2, result.size());
    }

    @Test
    void getPhaseOneContestsAndTypeOpenReturnsEmptyListWhenNoContestsExist() {
        when(contestRepository.findAllByPhase_Phase1_AndType_Open()).thenReturn(List.of());

        List<Contest> result = contestService.getPhaseOneContestsAndTypeOpen();

        assertTrue(result.isEmpty());
    }

    @Test
    void getNotStartedContestsReturnsListOfContests() {
        List<Contest> contests = List.of(new Contest(), new Contest());
        when(contestRepository.findContestByPhase_NotStarted()).thenReturn(contests);

        List<Contest> result = contestService.getNotStartedContests();

        assertEquals(2, result.size());
    }

    @Test
    void getNotStartedContestsReturnsEmptyListWhenNoContestsExist() {
        when(contestRepository.findContestByPhase_NotStarted()).thenReturn(List.of());

        List<Contest> result = contestService.getNotStartedContests();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPhotosReturnsListOfPhotos() {
        List<String> photos = List.of("photo1.jpg", "photo2.jpg");
        when(contestRepository.getAllByPhotos()).thenReturn(photos);

        List<String> result = contestService.getAllPhotos();

        assertEquals(2, result.size());
    }

    @Test
    void getAllPhotosReturnsEmptyListWhenNoPhotosExist() {
        when(contestRepository.getAllByPhotos()).thenReturn(List.of());

        List<String> result = contestService.getAllPhotos();

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteContestRemovesContestAndUpdatesUsers() {
        Contest contest = new Contest();
        contest.setType(Type.INVITATIONAL);
        contest.setId(1L);
        User jury = new User();
        jury.setJurorContests(new ArrayList<>(List.of(contest)));
        contest.setJurorContests(new ArrayList<>(List.of(jury)));
        User invitedUser = new User();
        invitedUser.setParticipatedContests(new ArrayList<>(List.of(contest)));
        contest.setInvitedUsers(new ArrayList<>(List.of(invitedUser)));
        User participant = new User();
        participant.setParticipatedContests(new ArrayList<>(List.of(contest)));
        contest.setParticipants(new ArrayList<>(List.of(participant)));

        when(contestRepository.findById(1L)).thenReturn(Optional.of(contest));

        contestService.deleteContest(1L);

        verify(userService, times(1)).save(jury);
        verify(userService, times(2)).save(invitedUser);
        verify(contestRepository).delete(contest);
    }

    @Test
    void deleteContestThrowsEntityNotFoundExceptionWhenContestDoesNotExist() {
        when(contestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> contestService.deleteContest(1L));
    }

    @Test
    void getInvitedContestsReturnsListOfContests() {
        User user = new User();
        user.setId(1L);
        List<Contest> contests = List.of(new Contest(), new Contest());
        when(contestRepository.findContestsByInvitedUsersEqualsUserId(1L)).thenReturn(contests);

        List<Contest> result = contestService.getInvitedContests(user);

        assertEquals(2, result.size());
    }

    @Test
    void getInvitedContestsReturnsEmptyListWhenNoContestsExist() {
        User user = new User();
        user.setId(1L);
        when(contestRepository.findContestsByInvitedUsersEqualsUserId(1L)).thenReturn(List.of());

        List<Contest> result = contestService.getInvitedContests(user);

        assertTrue(result.isEmpty());
    }



    @Test
    void getAllContestsReturnsEmptyListWhenNoContestsExist() {
        when(contestRepository.findAll()).thenReturn(List.of());

        List<Contest> result = contestService.getAllContests();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllContestsReturnsListOfContestsWhenContestsExist() {
        List<Contest> contests = List.of(new Contest(), new Contest());
        when(contestRepository.findAll()).thenReturn(contests);

        List<Contest> result = contestService.getAllContests();

        assertEquals(2, result.size());
    }

    @Test
    void findByTitleReturnsContestWhenTitleExists() {
        Contest contest = new Contest();
        when(contestRepository.findByTitle("Existing Title")).thenReturn(Optional.of(contest));

        Optional<Contest> result = contestService.findByTitle("Existing Title");

        assertTrue(result.isPresent());
    }

    @Test
    void findByTitleReturnsEmptyWhenTitleDoesNotExist() {
        when(contestRepository.findByTitle("Non-Existing Title")).thenReturn(Optional.empty());

        Optional<Contest> result = contestService.findByTitle("Non-Existing Title");

        assertTrue(result.isEmpty());
    }

    @Test
    void getOpenContestsReturnsListOfOpenContests() {
        List<Contest> contests = List.of(new Contest(), new Contest());
        when(contestRepository.findAllByPhase_Phase1()).thenReturn(contests);

        List<Contest> result = contestService.getOpenContests();

        assertEquals(2, result.size());
    }

    @Test
    void getAllParticipatedContestsReturnsListOfParticipatedContests() {
        User user = new User();
        user.setId(1L);
        List<Contest> contests = List.of(new Contest(), new Contest());
        when(contestRepository.findAllParticipated(1L)).thenReturn(contests);

        List<Contest> result = contestService.getAllParticipatedContests(user);

        assertEquals(2, result.size());
    }

    @Test
    void getFinishedContestsReturnsListOfFinishedContests() {
        User user = new User();
        user.setId(1L);
        List<Contest> contests = List.of(new Contest(), new Contest());
        when(contestRepository.findAllFinished(1L)).thenReturn(contests);

        List<Contest> result = contestService.getFinishedContests(user);

        assertEquals(2, result.size());
    }

    @Test
    void checkIfUserHasAlreadySubmittedPhotoReturnsTrueWhenUserHasSubmitted() {
        User user = new User();
        user.setId(1L);
        Contest contest = new Contest();
        when(contestRepository.findAllParticipated(1L)).thenReturn(List.of(contest));

        boolean result = contestService.checkIfUserHasAlreadySubmittedPhoto(user, contest);

        assertTrue(result);
    }

    @Test
    void checkIfUserHasAlreadySubmittedPhotoReturnsFalseWhenUserHasNotSubmitted() {
        User user = new User();
        user.setId(1L);
        Contest contest = new Contest();
        when(contestRepository.findAllParticipated(1L)).thenReturn(List.of());

        boolean result = contestService.checkIfUserHasAlreadySubmittedPhoto(user, contest);

        assertFalse(result);
    }

    @Test
    void saveSavesContest() {
        Contest contest = new Contest();

        contestService.save(contest);

        verify(contestRepository).save(contest);
    }



    @Test
    void getAllContestsByOptionsReturnsListOfContestsWhenOptionsMatch() {
        ContestFilterOptions filterOptions = new ContestFilterOptions();
        filterOptions.setTitle("Title");
        Category category = new Category();
        category.setName("Nature");
        filterOptions.setCategory(category.getName());
        filterOptions.setType(Type.INVITATIONAL);
        filterOptions.setPhase(Phase.PHASE_1);
        filterOptions.setUsername("username");

        List<Contest> contests = List.of(new Contest(), new Contest());
        when(contestRepository.findAllByFilterOptions("Title", filterOptions.getCategory(), Type.INVITATIONAL, Phase.PHASE_1, "username")).thenReturn(contests);

        List<Contest> result = contestService.getAllContestsByOptions(filterOptions);

        assertEquals(2, result.size());
    }

    @Test
    void getAllContestsByOptionsReturnsEmptyListWhenNoOptionsMatch() {
        ContestFilterOptions filterOptions = new ContestFilterOptions();
        filterOptions.setTitle("Title");
        Category category = new Category();
        category.setName("Nature");
        filterOptions.setCategory(category.getName());
        filterOptions.setType(Type.INVITATIONAL);
        filterOptions.setPhase(Phase.PHASE_1);
        filterOptions.setUsername("username");

        when(contestRepository.findAllByFilterOptions("Title", filterOptions.getCategory(), Type.INVITATIONAL, Phase.PHASE_1, "username")).thenReturn(List.of());

        List<Contest> result = contestService.getAllContestsByOptions(filterOptions);

        assertTrue(result.isEmpty());
    }

    @Test
    void getContestByCategoryNameReturnsListOfContestsWhenCategoryExists() {
        Category category = new Category();
        category.setId(1L);
        List<Contest> contests = List.of(new Contest(), new Contest());
        when(contestRepository.findContestByCategory_Id(1L)).thenReturn(contests);

        List<Contest> result = contestService.getContestByCategoryName(category);

        assertEquals(2, result.size());
    }

    @Test
    void getContestByCategoryNameThrowsEntityNotFoundExceptionWhenCategoryDoesNotExist() {
        Category category = new Category();
        category.setId(1L);
        when(contestRepository.findContestByCategory_Id(1L)).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> contestService.getContestByCategoryName(category));
    }

    @Test
    void getPhaseOneContestsReturnsListOfPhaseOneContests() {
        List<Contest> contests = List.of(new Contest(), new Contest());
        when(contestRepository.findAllByPhase_Phase1()).thenReturn(contests);

        List<Contest> result = contestService.getPhaseOneContests();

        assertEquals(2, result.size());
    }

    @Test
    void getPhaseTwoContestsReturnsListOfPhaseTwoContests() {
        List<Contest> contests = List.of(new Contest(), new Contest());
        when(contestRepository.findAllByPhase_Phase2()).thenReturn(contests);

        List<Contest> result = contestService.getPhaseTwoContests();

        assertEquals(2, result.size());
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

    @Test
    void inviteUserToContestInvitesUserWhenUserIsOrganizer() {
        User user = new User();
        user.setParticipatedContests(new ArrayList<>());
        user.setOrganizedContests(new ArrayList<>());
        user.setRole(UserRole.ORGANIZER);


        Contest contest = new Contest();
        contest.setType(Type.INVITATIONAL);
        contest.setInvitedUsers(new ArrayList<>());
        contest.setParticipants(new ArrayList<>());
      // Initialize the type field
        user.getOrganizedContests().add(contest);

        User userToInvite = new User();
        userToInvite.setRole(UserRole.JUNKIE);
        userToInvite.setParticipatedContests(new ArrayList<>());

        when(contestRepository.findById(1L)).thenReturn(Optional.of(contest));
        when(userService.saveUser(userToInvite)).thenReturn(userToInvite);

        Contest result = contestService.inviteUserToContest(1L, userToInvite, user);

        assertNotNull(result);
    }

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

    @Test
    void inviteUserToContestThrowsEntityDuplicateExceptionWhenUserAlreadyInvited() {
        // Create an organizer user
        User organizer = new User();
        organizer.setRole(UserRole.ORGANIZER);
        organizer.setOrganizedContests(new ArrayList<>());

        // Create a contest
        Contest contest = new Contest();
        contest.setType(Type.INVITATIONAL);
        contest.setParticipants(new ArrayList<>());
        contest.setInvitedUsers(new ArrayList<>());

        // Add the contest to the organizer's organized contests
        organizer.getOrganizedContests().add(contest);

        // Create a user to invite
        User userToInvite = new User();
        userToInvite.setRole(UserRole.JUNKIE);
        userToInvite.setParticipatedContests(new ArrayList<>());

        // Add the user to the contest's invited users list
        contest.getInvitedUsers().add(userToInvite);

        // Add the contest to the userToInvite's participatedContests
        userToInvite.getParticipatedContests().add(contest);

        // Mock the contestRepository to return our contest
        when(contestRepository.findById(1L)).thenReturn(Optional.of(contest));

        // Assert that an EntityDuplicateException is thrown
        assertThrows(EntityDuplicateException.class, () -> contestService.inviteUserToContest(1L, userToInvite, organizer));
    }

//    @Test
//    void calculateFinalContestPointsReturnsTop3UsersWithScores() {
//        List<PhotoSubmission> submissions = List.of(
//                new PhotoSubmission(),
//                new PhotoSubmission(),
//                new PhotoSubmission()
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
