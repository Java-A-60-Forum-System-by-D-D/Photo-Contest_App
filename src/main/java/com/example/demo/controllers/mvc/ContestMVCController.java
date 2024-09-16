package com.example.demo.controllers.mvc;

import com.example.demo.models.*;
import com.example.demo.models.dto.ContestViewDto;
import com.example.demo.models.dto.PhotoSubmissionDto;
import com.example.demo.models.dto.PhotoSubmissionMVCDto;
import com.example.demo.models.mappers.ContestMapper;
import com.example.demo.models.mappers.PhotoMapper;
import com.example.demo.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/contests")
public class ContestMVCController {
    private final ContestService contestService;
    private final ContestMapper contestMapper;
    private final UserService userService;
    private final PhotoSubmissionService photoSubmissionService;
    private final PhotoMapper photoMapper;
    private final PhotoReviewService photoReviewService;
    private final NotificationService notificationService;

    public ContestMVCController(ContestService contestService, ContestMapper contestMapper, UserService userService, PhotoSubmissionService photoSubmissionService, PhotoMapper photoMapper, PhotoReviewService photoReviewService, NotificationService notificationService) {
        this.contestService = contestService;
        this.contestMapper = contestMapper;
        this.userService = userService;
        this.photoSubmissionService = photoSubmissionService;
        this.photoMapper = photoMapper;
        this.photoReviewService = photoReviewService;
        this.notificationService = notificationService;
    }

    @ModelAttribute("notifications")
    public List<Notification> getNotifications(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return notificationService.getUnreadNotifications(user);
    }

    @GetMapping()
    public String contests() {
        return "contests";
    }

    @GetMapping("/{id}")
    public String contest(@PathVariable long id, Model model, Principal principal) {
        Contest contest = contestService.getContestById(id);
        User user = userService.getUserByEmail(principal.getName());
        ContestViewDto contestViewDto = contestMapper.createContestViewDto(contest);
        Boolean isFound = false;

        Optional<PhotoSubmission> photoSubmission = contest.getSubmissions()
                .stream()
                .filter(s ->
                        s.getCreator()
                                .getId() == user.getId()
                )
                .findFirst();
        if (photoSubmission.isPresent()) {
            isFound = true;
        }


        List<PhotoSubmission> submissions = contest.getSubmissions();
        submissions = submissions.stream().sorted(Comparator.comparing(PhotoSubmission::getReviewScore).reversed()).toList();
        Map<Long, Boolean> reviewStatusMap = new HashMap<>();
        for (PhotoSubmission submission : submissions) {
            boolean hasReviewed = submission.getReviews()
                    .stream()
                    .anyMatch(review -> review.getJury()
                            .getId() == user.getId());
            reviewStatusMap.put(submission.getId(), hasReviewed);
        }


        /*todo need to figure out how to handle invitational users*/
        boolean contains = false;
        if (user.getRole().equals(UserRole.ORGANIZER) || user.getJurorContests()
                .stream()
                .anyMatch(c -> c.getId() == contest.getId())) {
            contains = true;
        }


        model.addAttribute("phase", contest.getPhase());
        model.addAttribute("contest", contestViewDto);
        model.addAttribute("id", contest.getId());
        model.addAttribute("user", user);
        model.addAttribute("contains", contains);
        model.addAttribute("IsFound", isFound);
        model.addAttribute("submissions", submissions);
        model.addAttribute("reviewStatusMap", reviewStatusMap);


        return "contest-details";
    }

    @GetMapping("/{id}/submissions")
    public String contestSubmissions(@PathVariable long id, Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        Contest contest = contestService.getContestById(id);
        model.addAttribute("contest", contest);
        model.addAttribute("user", user);
        model.addAttribute("submission", new PhotoSubmissionMVCDto());

        return "photo-submission";
    }

    @PostMapping("/{id}/submissions")
    public String contestSubmissions(@PathVariable long id,
                                     @ModelAttribute("submission") PhotoSubmissionMVCDto photoSubmissionMVCDto,
                                     Model model,
                                     Principal principal) throws IOException {


        User user = userService.getUserByEmail(principal.getName());
        Contest contest = contestService.getContestById(id);
        PhotoSubmission photoSubmission = photoMapper.createPhotoSubmissionMVCFromDto(photoSubmissionMVCDto, user, contest);
        photoSubmissionService.createPhotoSubmission(photoSubmission, user);
        model.addAttribute("id", contest.getId());

        return "redirect:/contests/{id}";

    }

    @GetMapping("/juryPanel")
    public String juryPanel(Model model,
                            Principal principal) {

        User user = userService.getUserByEmail(principal.getName());

        model.addAttribute("contests", user.getJurorContests());
        model.addAttribute("user", user);
        return "jury-panel";
    }


}
