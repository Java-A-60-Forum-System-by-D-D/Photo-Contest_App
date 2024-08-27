package com.example.demo.models.mappers;

import com.example.demo.models.Contest;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.User;
import com.example.demo.models.dto.PhotoSubmissionDto;
import com.example.demo.models.dto.PhotoSubmissionViewDto;
import jakarta.persistence.Column;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

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
}
