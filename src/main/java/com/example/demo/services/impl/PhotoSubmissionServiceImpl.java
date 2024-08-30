package com.example.demo.services.impl;

import com.example.demo.exceptions.AuthorizationUserException;
import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.*;
import com.example.demo.models.filtering.PhotoSubmissionFilterOptions;
import com.example.demo.models.filtering.PhotoSubmissionSpecification;
import com.example.demo.repositories.PhotoSubmissionRepository;
import com.example.demo.services.ContestService;
import com.example.demo.services.PhotoSubmissionService;
import com.example.demo.services.UserService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PhotoSubmissionServiceImpl implements PhotoSubmissionService {
    public static final String PHOTO_SUBMISSION_NOT_FOUND = "Photo submission not found: ";
    private final PhotoSubmissionRepository photoSubmissionRepository;
    private final UserService userService;
    private final ContestService contestService;

    public PhotoSubmissionServiceImpl(PhotoSubmissionRepository photoSubmissionRepository, UserService userService, ContestService contestService) {
        this.photoSubmissionRepository = photoSubmissionRepository;

        this.userService = userService;
        this.contestService = contestService;
    }

    @Override
    public List<PhotoSubmission> getAllPhotoSubmissions() {

        return photoSubmissionRepository.findAll();
    }

    @Override
    public List<PhotoSubmission> getPhotoSubmissions(PhotoSubmissionFilterOptions filterOptions) {
        Specification<PhotoSubmission> spec = PhotoSubmissionSpecification.filterByOptions(filterOptions);
        return photoSubmissionRepository.findAll(spec);
    }

    @Override
    @Transactional
    public PhotoSubmission createPhotoSubmission(PhotoSubmission photoSubmission, User user) {
        if (user.getParticipatedContests()
                .contains(photoSubmission.getContest())) {
            throw new EntityDuplicateException("User already submitted photo for this contest");
        }
        if (!photoSubmission.getContest()
                .getPhase()
                .equals(Phase.PHASE_1)) {
            throw new EntityDuplicateException("Contest is not in phase 1");
        }
        Contest currentContest = photoSubmission.getContest();
        if (user.getJurorContests().contains(currentContest)) {
            throw new AuthorizationUserException("User is a juror for this contest");
        }
        if (currentContest.getType().equals(Type.INVITATIONAL)) {
            if (currentContest.getInvitedUsers().contains(user)) {
                throw new AuthorizationUserException("User is not invited to this contest");

            }
        }

            user.getParticipatedContests()
                    .add(photoSubmission.getContest());
            user.setTotalScore(currentContest.getInvitedUsers().contains(user)
                    ? user.getTotalScore() + 3
                    : user.getTotalScore() + 1);
            userService.calculateLevel(user);
            userService.save(user);

            Contest contest = photoSubmission.getContest();
            contest.getParticipants()
                    .add(user);
            contestService.save(contest);


            return photoSubmissionRepository.save(photoSubmission);
        }

        @Override
        public List<PhotoSubmission> getAllReviewedPhotos (PhotoSubmission photoSubmission, User user){
            return photoSubmissionRepository.findByJuriIdAndReviewed(user.getId());
        }


        @Override
        public PhotoSubmission getPhotoSubmissionById ( long id){
            return photoSubmissionRepository.findById(id)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException(PHOTO_SUBMISSION_NOT_FOUND + id));
        }

        @Override
        public List<PhotoSubmission> getNotReviewedPhotos () {
            return photoSubmissionRepository.findNotReviewedPhotos();
        }

        @Override
        public void save (PhotoSubmission photoSubmission){
            photoSubmissionRepository.save(photoSubmission);
        }

        @Override
        public List<PhotoSubmission> getAScoreAndComments (User user){
//        if(!contest.getPhase().equals(Phase.FINISHED)){
//            throw new EntityNotFoundException("Contest is not finished");
//        }
            return photoSubmissionRepository.findPhotoSubmissionsByCreator_IdAndContest_Phase_Finished(user.getId(), Phase.FINISHED);
        }

        @Override
        public List<PhotoSubmission> findAllContestSubmissionsByOthers (User user){
//        if(!contest.getPhase().equals(Phase.FINISHED)){
//            throw new EntityNotFoundException("Contest is not finished");
//        }
            return photoSubmissionRepository.findPhotoSubmissionsCreatedByOtherUsers(user.getId(), Phase.FINISHED);
        }
    }
