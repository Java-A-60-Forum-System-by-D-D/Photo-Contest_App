package com.example.demo.controllers.rest;

import com.example.demo.models.Contest;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.User;
import com.example.demo.models.dto.ContestDto;
import com.example.demo.models.dto.ContestViewDto;
import com.example.demo.models.dto.PhotoSubmissionDto;
import com.example.demo.models.dto.PhotoSubmissionViewDto;
import com.example.demo.models.mappers.ContestMapper;
import com.example.demo.models.mappers.PhotoMapper;
import com.example.demo.services.ContestService;
import com.example.demo.services.PhotoSubmissionService;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contests")
public class ContestRestController {
    private final ContestService contestService;
    private final UserService userService;
    private final ContestMapper contestMapper;
    private final PhotoMapper photoMapper;
    private final PhotoSubmissionService photoSubmissionService;

    public ContestRestController(ContestService contestService, UserService userService, ContestMapper contestMapper, PhotoMapper photoMapper, PhotoSubmissionService photoSubmissionService) {
        this.contestService = contestService;
        this.userService = userService;
        this.contestMapper = contestMapper;
        this.photoMapper = photoMapper;
        this.photoSubmissionService = photoSubmissionService;
    }

    @GetMapping
    public Map<String, List<ContestViewDto>> getAllContests() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        List<ContestViewDto> openContests = contestService.getOpenContests()
                .stream()
                .map(contestMapper::createContestViewDto).toList();
        List<ContestViewDto> participatedContests = contestService.getAllParticipatedContests(user)
                .stream()
                .map(contestMapper::createContestViewDto).toList();
        List<ContestViewDto> finishedContests = contestService.getFinishedContests(user)
                .stream()
                .map(contestMapper::createContestViewDto).toList();
        return Map.of("open", openContests, "participated", participatedContests, "finished", finishedContests);
    }

    @PostMapping
    public ContestViewDto createContest(@Valid @RequestBody ContestDto contestDto) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        Contest contest = contestMapper.createContestFromDto(contestDto, user);
        contestService.createContest(contest, user);
        return contestMapper.createContestViewDto(contest);

    }

    @GetMapping("/{id}")
    public ContestViewDto getContestById(@PathVariable long id) {
        Contest contest = contestService.getContestById(id);
        return contestMapper.createContestViewDto(contest);
    }

    @GetMapping("/{id}/photo-submissions")
    public List<PhotoSubmissionViewDto> getAllPhotoSubmissionsByContestId(@PathVariable long id) {
        Contest contest = contestService.getContestById(id);
        List<PhotoSubmissionViewDto> photoSubmissions = contest.getSubmissions()
                .stream()
                .map(photoMapper::toPhotoSubmissionViewDto)
                .toList();
        return photoSubmissions;
    }

    @PostMapping("/{id}/photo-submissions")
    public PhotoSubmissionViewDto createPhotoSubmission(@PathVariable("id") long id, @Valid @RequestBody PhotoSubmissionDto photoSubmissionDto) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        Contest contest = contestService.getContestById(id);
        //todo check Contest Phase (if it is PHASE_1)
        PhotoSubmission photoSubmission = photoMapper.createPhotoSubmissionFromDto(photoSubmissionDto, user, contest);
        photoSubmissionService.createPhotoSubmission(photoSubmission, user);

        return photoMapper.toPhotoSubmissionViewDto(photoSubmission);
    }
}
