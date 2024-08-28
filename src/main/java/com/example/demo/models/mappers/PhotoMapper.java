package com.example.demo.models.mappers;

import com.example.demo.models.Contest;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.User;
import com.example.demo.models.dto.PhotoSubmissionDto;
import com.example.demo.models.dto.PhotoSubmissionReviewsView;
import com.example.demo.models.dto.PhotoSubmissionViewDto;
import com.example.demo.models.dto.ReviewView;
import jakarta.persistence.Column;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PhotoMapper {
    private final ModelMapper modelMapper;

    public PhotoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PhotoSubmissionViewDto toPhotoSubmissionViewDto(PhotoSubmission photoSubmission) {
        PhotoSubmissionViewDto photoSubmissionViewDto = modelMapper.map(photoSubmission, PhotoSubmissionViewDto.class);
        photoSubmissionViewDto.setUserNames(photoSubmission.getCreator().getFirstName() + " " + photoSubmission.getCreator().getLastName());
        photoSubmissionViewDto.setContestTitle(photoSubmission.getContest().getTitle());
        return photoSubmissionViewDto;
    }

    public PhotoSubmission createPhotoSubmissionFromDto(PhotoSubmissionDto photoSubmissionDto, User user, Contest contest) {
        PhotoSubmission photoSubmission = modelMapper.map(photoSubmissionDto, PhotoSubmission.class);
        photoSubmission.setCreator(user);
        photoSubmission.setContest(contest);
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
                    reviews.add(reviewView);
                        });


       photoSubmissionViewDto.setReviews(reviews);
       return photoSubmissionViewDto;
    }
}
