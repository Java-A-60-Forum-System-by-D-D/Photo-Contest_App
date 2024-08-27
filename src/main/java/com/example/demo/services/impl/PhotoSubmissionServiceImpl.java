package com.example.demo.services.impl;

import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.User;
import com.example.demo.repositories.PhotoSubmissionRepository;
import com.example.demo.services.PhotoSubmissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PhotoSubmissionServiceImpl implements PhotoSubmissionService {
    private final PhotoSubmissionRepository photoSubmissionRepository;

    public PhotoSubmissionServiceImpl(PhotoSubmissionRepository photoSubmissionRepository) {
        this.photoSubmissionRepository = photoSubmissionRepository;
    }

    @Override
    public List<PhotoSubmission> getAllPhotoSubmissions() {
        return photoSubmissionRepository.findAll();
    }

    @Override
    @Transactional
    public PhotoSubmission createPhotoSubmission(PhotoSubmission photoSubmission, User user) {
        if(user.getParticipatedContests().contains(photoSubmission.getContest())){
            throw new EntityDuplicateException("User already submitted photo for this contest");
        }

        System.out.println(user.getParticipatedContests());


        user.getParticipatedContests().add(photoSubmission.getContest());

        System.out.println(user.getParticipatedContests());

        photoSubmission.getContest().getParticipants().add(user);
        return photoSubmissionRepository.save(photoSubmission);
    }
}
