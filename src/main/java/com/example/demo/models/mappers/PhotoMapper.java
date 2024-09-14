package com.example.demo.models.mappers;

import com.example.demo.models.Contest;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.User;
import com.example.demo.models.dto.*;
import com.example.demo.services.CloudinaryImageService;
import jakarta.persistence.Column;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PhotoMapper {
    private final ModelMapper modelMapper;
    private final CloudinaryImageService cloudinaryImageService;

    public PhotoMapper(ModelMapper modelMapper, CloudinaryImageService cloudinaryImageService) {
        this.modelMapper = modelMapper;
        this.cloudinaryImageService = cloudinaryImageService;
    }

    public PhotoSubmissionViewDto toPhotoSubmissionViewDto(PhotoSubmission photoSubmission) {
        PhotoSubmissionViewDto photoSubmissionViewDto = modelMapper.map(photoSubmission, PhotoSubmissionViewDto.class);
        photoSubmissionViewDto.setUserNames(photoSubmission.getCreator().getFirstName() + " " + photoSubmission.getCreator().getLastName());
        photoSubmissionViewDto.setContestTitle(photoSubmission.getContest().getTitle());

        return photoSubmissionViewDto;
    }

    public PhotoSubmission createPhotoSubmissionFromDto(PhotoSubmissionDto photoSubmissionDto, User user, Contest contest) throws IOException {
        PhotoSubmission photoSubmission = modelMapper.map(photoSubmissionDto, PhotoSubmission.class);
        photoSubmission.setCreator(user);
        photoSubmission.setContest(contest);
        photoSubmission.setPhotoUrl(cloudinaryImageService.uploadImageFromUrl(photoSubmissionDto.getPhotoUrl()));
        return photoSubmission;
    }
    public PhotoSubmission createPhotoSubmissionMVCFromDto(PhotoSubmissionMVCDto photoSubmissionDto, User user, Contest contest) throws IOException {
        PhotoSubmission photoSubmission = modelMapper.map(photoSubmissionDto, PhotoSubmission.class);
        photoSubmission.setCreator(user);
        photoSubmission.setContest(contest);
        String photoUrl = cloudinaryImageService.uploadImage(photoSubmissionDto.getPhotoUrl());
        photoSubmission.setPhotoUrl(photoUrl);
        return photoSubmission;
    }
    public PhotoSubmissionReviewsView createPhotoSubmissionReviewView(PhotoSubmission photoSubmission) {
       PhotoSubmissionReviewsView photoSubmissionViewDto = modelMapper.map(photoSubmission, PhotoSubmissionReviewsView.class);
        List<ReviewView> reviews = new ArrayList<>();
        photoSubmission.getReviews()
                .forEach(r ->{
                    ReviewView reviewView = new ReviewView();
                    reviewView.setScore(r.getScore());
                    reviewView.setComment(r.getComment());
                    reviewView.setJuryNames(r.getJury().getFirstName() + " " + r.getJury().getLastName());
                    reviewView.setCategoryMismatch(r.isCategoryMismatch());
                    reviews.add(reviewView);
                        });


       photoSubmissionViewDto.setReviews(reviews);
       return photoSubmissionViewDto;
    }
}
