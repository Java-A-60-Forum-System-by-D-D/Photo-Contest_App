package com.example.demo.controllers.rest;

import com.example.demo.models.Contest;
import com.example.demo.models.User;
import com.example.demo.models.dto.ContestDto;
import com.example.demo.models.dto.ContestViewDto;
import com.example.demo.models.mappers.ContestMapper;
import com.example.demo.services.ContestService;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/contests")
public class ContestRestController {
    private final ContestService contestService;
    private final UserService userService;
    private final ContestMapper contestMapper;

    public ContestRestController(ContestService contestService, UserService userService, ContestMapper contestMapper) {
        this.contestService = contestService;
        this.userService = userService;
        this.contestMapper = contestMapper;
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
}
