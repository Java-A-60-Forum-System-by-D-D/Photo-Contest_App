package com.example.demo.controllers.mvc;

import com.example.demo.models.Contest;
import com.example.demo.models.User;
import com.example.demo.models.dto.ContestViewDto;
import com.example.demo.models.dto.PhotoSubmissionDto;
import com.example.demo.models.dto.PhotoSubmissionMVCDto;
import com.example.demo.models.mappers.ContestMapper;
import com.example.demo.services.ContestService;
import com.example.demo.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/contests")
public class ContestMVCController {
    private final ContestService contestService;
    private final ContestMapper contestMapper;
    private final UserService userService;

    public ContestMVCController(ContestService contestService, ContestMapper contestMapper, UserService userService) {
        this.contestService = contestService;
        this.contestMapper = contestMapper;
        this.userService = userService;
    }

    @GetMapping()
    public String contests() {
        return "contests";
    }
    @GetMapping("/{id}")
    public String contest(@PathVariable Long id, Model model) {
        Contest contest = contestService.getContestById(id);
        ContestViewDto contestViewDto = contestMapper.createContestViewDto(contest);
        model.addAttribute("contest", contestViewDto);
        model.addAttribute("id", contest.getId());
        return "contest-details";
    }
    @GetMapping("/{id}/submissions")
    public String contestSubmissions(@PathVariable Long id, Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        Contest contest = contestService.getContestById(id);
        model.addAttribute("contest", contest);
        model.addAttribute("user", user);
        model.addAttribute("submission", new PhotoSubmissionMVCDto());
        return "photo-submission";
    }


}
