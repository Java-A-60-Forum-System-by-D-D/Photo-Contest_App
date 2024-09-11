package com.example.demo.controllers.mvc;

import com.example.demo.models.Contest;
import com.example.demo.models.dto.ContestViewDto;
import com.example.demo.models.mappers.ContestMapper;
import com.example.demo.services.ContestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contests")
public class ContestMVCController {
    private final ContestService contestService;
    private final ContestMapper contestMapper;

    public ContestMVCController(ContestService contestService, ContestMapper contestMapper) {
        this.contestService = contestService;
        this.contestMapper = contestMapper;
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
        return "contest-details";
    }
//    @PutMapping("/{id}")
//    public ContestViewDto updateContest(@PathVariable Long id) {
//        return new ContestViewDto();
//    }

}
