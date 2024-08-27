package com.example.demo.models.mappers;

import com.example.demo.models.PhotoReview;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.User;
import com.example.demo.models.dto.PhotoReviewDto;
import com.example.demo.models.dto.PhotoReviewView;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    private final ModelMapper modelMapper;

    public ReviewMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    public PhotoReview createPhotoReviewFromDto(PhotoReviewDto photoReviewDto, User user) {
        PhotoReview photoReview = modelMapper.map(photoReviewDto, PhotoReview.class);
        photoReview.setJury(user);
        photoReview.setReviewed(true);
        if(photoReviewDto.isCategoryMismatch()){
            photoReview.setCategoryMismatch(true);
            photoReview.setScore(0);
        }
        return photoReview;
    }

    public PhotoReviewView toPhotoReviewView(PhotoReview photoReview, PhotoSubmission photoSubmission) {

        PhotoReviewView photoReviewView = modelMapper.map(photoReview, PhotoReviewView.class);
        photoReviewView.setPhotoUrl(photoSubmission.getPhotoUrl());
        photoReviewView.setJuryNames(photoReview.getJury().getFirstName() + " " + photoReview.getJury().getLastName());
        return photoReviewView;
    }
}
