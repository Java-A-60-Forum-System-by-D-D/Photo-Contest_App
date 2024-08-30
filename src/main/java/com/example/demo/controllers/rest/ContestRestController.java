package com.example.demo.controllers.rest;

import com.example.demo.models.*;
import com.example.demo.models.dto.*;
import com.example.demo.models.filtering.ContestFilterOptions;
import com.example.demo.models.mappers.ContestMapper;
import com.example.demo.models.mappers.PhotoMapper;
import com.example.demo.services.CategoryService;
import com.example.demo.services.ContestService;
import com.example.demo.services.PhotoSubmissionService;
import com.example.demo.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contests")
@Tag(name = "contests", description = "Contest operations")
public class ContestRestController {
    private final ContestService contestService;
    private final UserService userService;
    private final ContestMapper contestMapper;
    private final PhotoMapper photoMapper;
    private final PhotoSubmissionService photoSubmissionService;
    private final CategoryService categoryService;

    public ContestRestController(ContestService contestService, UserService userService, ContestMapper contestMapper, PhotoMapper photoMapper, PhotoSubmissionService photoSubmissionService, CategoryService categoryService) {
        this.contestService = contestService;
        this.userService = userService;
        this.contestMapper = contestMapper;
        this.photoMapper = photoMapper;
        this.photoSubmissionService = photoSubmissionService;
        this.categoryService = categoryService;
    }

    @Operation(summary = "View a list of all contests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public Map<String, List<ContestSummaryDTO>> getAllContests(@RequestParam(required = false) String title,
                                                               @RequestParam(required = false) String category,
                                                               @RequestParam(required = false) String type,
                                                               @RequestParam(required = false) String phase,
                                                               @RequestParam(required = false) String username) {

        //todo add listing and filtering of contests
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
//        Category categoryToFind = categoryService.getCategoryByName(category);
        Phase phaseEnum = null;
        if (phase != null) {
            phaseEnum = Phase.valueOf(phase.toUpperCase());
        }
        Type typeEnum = null;
        if(type != null){
            typeEnum = Type.valueOf(type.toUpperCase());
        }
        ContestFilterOptions filterOptions = new ContestFilterOptions(title, category, typeEnum, phaseEnum,username);
        List<Contest> contests = contestService.getAllContestsByOptions(filterOptions);
//        List<ContestSummaryDTO> openContests = contestService.getOpenContests()
//                .stream()
//                .map(contestMapper::createFinishedContestViewDto)
//                .toList();
//        List<ContestSummaryDTO> participatedContests = contestService.getAllParticipatedContests(user)
//                .stream()
//                .map(contestMapper::createFinishedContestViewDto)
//                .toList();
//        List<ContestSummaryDTO> finishedContests = contestService.getFinishedContests(user)
//                .stream()
//                .map(contestMapper::createFinishedContestViewDto)
//                .toList();
       return Map.of("contests", contests.stream().map(contestMapper::createFinishedContestViewDto).toList());
    }

    @Operation(summary = "Create a new contest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created contest"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "409", description = "Contest with such title already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ContestViewDto createContest(
            @Parameter(description = "Contest details", required = true) @Valid @RequestBody ContestDto contestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        Contest contest = contestMapper.createContestFromDto(contestDto, user);
        contestService.createContest(contest, user);
        return contestMapper.createContestViewDto(contest);
    }

    @Operation(summary = "Get contest by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved contest"),
            @ApiResponse(responseCode = "404", description = "Contest not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ContestViewDto getContestById(
            @Parameter(description = "ID of the contest to retrieve", required = true) @PathVariable long id) {
        Contest contest = contestService.getContestById(id);
        return contestMapper.createContestViewDto(contest);
    }

    @Operation(summary = "Get all photo submissions by contest ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved photo submissions"),
            @ApiResponse(responseCode = "404", description = "Contest not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}/photo-submissions")
    public List<PhotoSubmissionViewDto> getAllPhotoSubmissionsByContestId(
            @Parameter(description = "ID of the contest to retrieve photo submissions for", required = true) @PathVariable long id) {
        Contest contest = contestService.getContestById(id);
        List<PhotoSubmissionViewDto> photoSubmissions = contest.getSubmissions()
                .stream()
                .map(photoMapper::toPhotoSubmissionViewDto)
                .toList();
        return photoSubmissions;
    }

    @Operation(summary = "Create a photo submission for a contest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created photo submission"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Contest not found"),
            @ApiResponse(responseCode = "409", description = "Contest phase is not PHASE_1"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/photo-submissions")
    public PhotoSubmissionViewDto createPhotoSubmission(
            @Parameter(description = "ID of the contest to submit photo for", required = true) @PathVariable("id") long id,
            @Parameter(description = "Photo submission details", required = true) @Valid @RequestBody PhotoSubmissionDto photoSubmissionDto) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        Contest contest = contestService.getContestById(id);
        //todo check Contest Phase (if it is PHASE_1)
        PhotoSubmission photoSubmission = photoMapper.createPhotoSubmissionFromDto(photoSubmissionDto, user, contest);

        photoSubmissionService.createPhotoSubmission(photoSubmission, user);

        return photoMapper.toPhotoSubmissionViewDto(photoSubmission);
    }

    @Operation(summary = "Add a jury to a contest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added jury"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Contest or user not found"),
            @ApiResponse(responseCode = "409", description = "User is already a jury for this contest"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ContestViewDto addJury(
            @Parameter(description = "ID of the contest to add jury to", required = true) @PathVariable("id") long id,
            @Parameter(description = "Jury details", required = true) @RequestBody JuryDTO juryDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mail = authentication.getName();
        User user = userService.getUserByEmail(mail);

        User juryToAdd = userService.getUserByEmail(juryDTO.getEmail());

        return contestMapper.createContestViewDto(contestService.addJury(id, juryToAdd, user));
    }

    @Operation(summary = "Get finished contests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved finished contests"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/finished")
    public Map<String, List<PhotoSubmissionReviewsView>> getFinishedContest() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mail = authentication.getName();
        User user = userService.getUserByEmail(mail);

        List<PhotoSubmission> getListOfScoresAndJuryComments = photoSubmissionService.getAScoreAndComments(user);
        List<PhotoSubmissionReviewsView> userView = getListOfScoresAndJuryComments
                .stream()
                .map(photoMapper::createPhotoSubmissionReviewView)
                .toList();
        List<PhotoSubmission> getListOfOtherUsersSubmissions = photoSubmissionService.findAllContestSubmissionsByOthers(user);
        List<PhotoSubmissionReviewsView> othersView = getListOfOtherUsersSubmissions
                .stream()
                .map(photoMapper::createPhotoSubmissionReviewView)
                .toList();
        return Map.of("user", userView, "others", othersView);
    }
}