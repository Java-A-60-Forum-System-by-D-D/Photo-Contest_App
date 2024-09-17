package com.example.demo.ServiceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.exceptions.AuthorizationUserException;
import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.*;
import com.example.demo.models.filtering.PhotoSubmissionFilterOptions;
import com.example.demo.repositories.PhotoSubmissionRepository;
import com.example.demo.services.ContestService;
import com.example.demo.services.UserService;
import com.example.demo.services.impl.PhotoSubmissionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

class PhotoSubmissionServiceImplTest {

    @Mock
    private PhotoSubmissionRepository photoSubmissionRepository;

    @Mock
    private UserService userService;

    @Mock
    private ContestService contestService;

    @InjectMocks
    private PhotoSubmissionServiceImpl photoSubmissionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPhotoSubmissionsReturnsAllSubmissions() {
        PhotoSubmission submission = new PhotoSubmission();
        when(photoSubmissionRepository.findAll()).thenReturn(List.of(submission));

        List<PhotoSubmission> result = photoSubmissionService.getAllPhotoSubmissions();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(submission, result.get(0));
    }
    @Test
    void getPhotoSubmissionsReturnsFilteredSubmissions() {
        PhotoSubmissionFilterOptions filterOptions = new PhotoSubmissionFilterOptions();
        PhotoSubmission submission = new PhotoSubmission();
        Specification<PhotoSubmission> spec = mock(Specification.class);
        when(photoSubmissionRepository.findAll(any(Specification.class))).thenReturn(List.of(submission));

        List<PhotoSubmission> result = photoSubmissionService.getPhotoSubmissions(filterOptions);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(submission, result.get(0));
    }

    @Test
    void createPhotoSubmissionThrowsExceptionWhenUserAlreadySubmitted() {
        User user = new User();
        Contest contest = new Contest();
        PhotoSubmission submission = new PhotoSubmission();
        submission.setContest(contest);
        List<Contest> participatedContests = new ArrayList<>();
        participatedContests.add(contest);

        user.setParticipatedContests(participatedContests); // Correctly initialize participatedContests

        assertThrows(EntityDuplicateException.class, () -> photoSubmissionService.createPhotoSubmission(submission, user));
    }

    @Test
    void createPhotoSubmissionThrowsExceptionWhenContestNotInPhase1() {
        User user = new User();
        user.setParticipatedContests(Collections.emptyList()); // Initialize participatedContests
        Contest contest = new Contest();
        contest.setPhase(Phase.PHASE_2);
        PhotoSubmission submission = new PhotoSubmission();
        submission.setContest(contest);

        assertThrows(AuthorizationUserException.class, () -> photoSubmissionService.createPhotoSubmission(submission, user));
    }

    @Test
    void createPhotoSubmissionThrowsExceptionWhenUserIsJuror() {
        User user = new User();
        user.setParticipatedContests(new ArrayList<>()); // Initialize participatedContests
        Contest contest = new Contest();
        PhotoSubmission submission = new PhotoSubmission();
        submission.setContest(contest);
        List<Contest> jurorContests = new ArrayList<>();
        jurorContests.add(contest);

        user.setJurorContests(jurorContests); // Correctly initialize jurorContests

        assertThrows(AuthorizationUserException.class, () -> photoSubmissionService.createPhotoSubmission(submission, user));
    }

    @Test
    void createPhotoSubmissionThrowsExceptionWhenUserNotInvitedToInvitationalContest() {
        User user = new User();
        user.setParticipatedContests(Collections.emptyList()); // Initialize participatedContests
        user.setJurorContests(Collections.emptyList()); // Initialize jurorContests
        Contest contest = new Contest();
        contest.setType(Type.INVITATIONAL);
        PhotoSubmission submission = new PhotoSubmission();
        submission.setContest(contest);

        assertThrows(AuthorizationUserException.class, () -> photoSubmissionService.createPhotoSubmission(submission, user));
    }

    @Test
    void createPhotoSubmissionSavesSubmissionWhenValid() {
        User user = new User();
        user.setParticipatedContests(new ArrayList<>()); // Initialize participatedContests
        Contest contest = new Contest();
        contest.setParticipants(new ArrayList<>());
        contest.setPhase(Phase.PHASE_1); // Ensure contest is in phase 1
        contest.setType(Type.OPEN); // Initialize type
        PhotoSubmission submission = new PhotoSubmission();
        submission.setContest(contest);

        when(photoSubmissionRepository.save(submission)).thenReturn(submission);

        PhotoSubmission result = photoSubmissionService.createPhotoSubmission(submission, user);

        assertNotNull(result);
        assertEquals(submission, result);
    }

    @Test
    void getAllReviewedPhotosReturnsReviewedPhotos() {
        User user = new User();
        PhotoSubmission submission = new PhotoSubmission();
        when(photoSubmissionRepository.findByJuriIdAndReviewed(user.getId())).thenReturn(List.of(submission));

        List<PhotoSubmission> result = photoSubmissionService.getAllReviewedPhotos(submission, user);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(submission, result.get(0));
    }

    @Test
    void getPhotoSubmissionByIdReturnsSubmissionWhenFound() {
        PhotoSubmission submission = new PhotoSubmission();
        when(photoSubmissionRepository.findById(1L)).thenReturn(Optional.of(submission));

        PhotoSubmission result = photoSubmissionService.getPhotoSubmissionById(1L);

        assertNotNull(result);
        assertEquals(submission, result);
    }

    @Test
    void getPhotoSubmissionByIdThrowsExceptionWhenNotFound() {
        when(photoSubmissionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> photoSubmissionService.getPhotoSubmissionById(1L));
    }

    @Test
    void getNotReviewedPhotosReturnsNotReviewedPhotos() {
        PhotoSubmission submission = new PhotoSubmission();
        when(photoSubmissionRepository.findNotReviewedPhotos()).thenReturn(List.of(submission));

        List<PhotoSubmission> result = photoSubmissionService.getNotReviewedPhotos();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(submission, result.get(0));
    }

    @Test
    void saveSavesPhotoSubmission() {
        PhotoSubmission submission = new PhotoSubmission();

        photoSubmissionService.save(submission);

        verify(photoSubmissionRepository, times(1)).save(submission);
    }

    @Test
    void getAScoreAndCommentsReturnsFinishedSubmissions() {
        User user = new User();
        PhotoSubmission submission = new PhotoSubmission();
        when(photoSubmissionRepository.findPhotoSubmissionsByCreator_IdAndContest_Phase_Finished(user.getId(), Phase.FINISHED)).thenReturn(List.of(submission));

        List<PhotoSubmission> result = photoSubmissionService.getAScoreAndComments(user);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(submission, result.get(0));
    }

    @Test
    void findAllContestSubmissionsByOthersReturnsSubmissionsByOthers() {
        User user = new User();
        PhotoSubmission submission = new PhotoSubmission();
        when(photoSubmissionRepository.findPhotoSubmissionsCreatedByOtherUsers(user.getId(), Phase.FINISHED)).thenReturn(List.of(submission));

        List<PhotoSubmission> result = photoSubmissionService.findAllContestSubmissionsByOthers(user);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(submission, result.get(0));
    }
}