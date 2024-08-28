package com.example.demo.models.mappers;

import com.example.demo.models.Category;
import com.example.demo.models.Contest;
import com.example.demo.models.User;
import com.example.demo.models.dto.ContestDto;
import com.example.demo.models.dto.ContestViewDto;
import com.example.demo.services.CategoryService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class ContestMapper {
    private final CategoryService categoryService;

    public ContestMapper(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public Contest createContestFromDto(ContestDto contestDto, User user) {
        Contest contest = new Contest();
        contest.setTitle(contestDto.getTitle());
        contest.setDescription(contestDto.getDescription());
        contest.setCategory(categoryService.getCategoryByName(contestDto.getCategory()));
        contest.setCreatedAt(LocalDateTime.now());
        contest.setUpdatedAt(LocalDateTime.now());
        contest.setOrganizer(user);
        LocalDateTime startPhase1 = LocalDateTime.now().with(LocalTime.of(22, 0));
        contest.setStartPhase1(startPhase1);
        LocalDateTime startPhase2 = startPhase1.plusDays(2);
        contest.setStartPhase2(startPhase2);
        LocalDateTime startPhase3 = startPhase2.plusHours(12);
        contest.setStartPhase3(startPhase3);
        contest.setOpen(contestDto.getIsOpen());
        return contest;
    }
    public ContestViewDto createContestViewDto(Contest contest) {
        ContestViewDto contestViewDto = new ContestViewDto();
        contestViewDto.setTitle(contest.getTitle());
        contestViewDto.setDescription(contest.getDescription());
        contestViewDto.setCategory(contest.getCategory().getName());
        contestViewDto.setCreatorName(contest.getOrganizer().getFirstName() + " " + contest.getOrganizer().getLastName());
        contestViewDto.setStartPhase1(contest.getStartPhase1());
        contestViewDto.setStartPhase2(contest.getStartPhase2());
        contestViewDto.setStartPhase3(contest.getStartPhase3());
        contestViewDto.setIsOpen(contest.isOpen());
        return contestViewDto;
    }
}
