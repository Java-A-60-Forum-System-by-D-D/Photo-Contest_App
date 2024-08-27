package com.example.demo.services.impl;

import com.example.demo.exceptions.AuthorizationUserException;
import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Contest;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.repositories.ContestRepository;
import com.example.demo.repositories.PhotoSubmissionRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.ContestService;
import com.example.demo.services.PhotoSubmissionService;
import com.example.demo.services.UserService;
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
    @Transactional
    public PhotoSubmission createPhotoSubmission(PhotoSubmission photoSubmission, User user) {
        if (user.getParticipatedContests()
                .contains(photoSubmission.getContest())) {
            throw new EntityDuplicateException("User already submitted photo for this contest");
        }


        user.getParticipatedContests()
            .add(photoSubmission.getContest());

        userService.save(user);

        Contest contest = photoSubmission.getContest();
        contest.getParticipants()
               .add(user);
        contestService.save(contest);


        return photoSubmissionRepository.save(photoSubmission);
    }

    @Override
    public List<PhotoSubmission> getAllReviewedPhotos(PhotoSubmission photoSubmission, User user) {
        return photoSubmissionRepository.findByJuriIdAndReviewed(user.getId());
    }



    @Override
    public PhotoSubmission getPhotoSubmissionById(long id) {
        return photoSubmissionRepository.findById(id).stream().findFirst()
                                        .orElseThrow(() -> new EntityNotFoundException(PHOTO_SUBMISSION_NOT_FOUND + id));
    }

    @Override
    public List<PhotoSubmission> getNotReviewedPhotos() {
        return photoSubmissionRepository.findNotReviewedPhotos();
    }

    @Override
    public void save(PhotoSubmission photoSubmission) {
        photoSubmissionRepository.save(photoSubmission);
    }
}
