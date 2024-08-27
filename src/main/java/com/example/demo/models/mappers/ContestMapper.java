package com.example.demo.models.mappers;

import com.example.demo.models.Contest;
import com.example.demo.models.User;
import com.example.demo.models.dto.ContestDto;
import com.example.demo.services.CategoryService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
        contest.setOrganizer(user);

        return contest;
    }
}
