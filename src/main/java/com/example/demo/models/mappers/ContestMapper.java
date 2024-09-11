package com.example.demo.models.mappers;

import com.example.demo.models.Contest;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.Type;
import com.example.demo.models.User;
import com.example.demo.models.dto.ContestDto;
import com.example.demo.models.dto.ContestSummaryDTO;
import com.example.demo.models.dto.ContestViewDto;
import com.example.demo.models.dto.PlaceDTO;
import com.example.demo.services.CategoryService;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

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
        contest.setPhotoUrl(categoryService.getCategoryByName(contestDto.getCategory())
                                           .getPhotoUrl());
        contest.setCreatedAt(LocalDateTime.now());
        contest.setUpdatedAt(LocalDateTime.now());
        contest.setOrganizer(user);
        LocalDateTime startPhase1 = LocalDateTime.now()
                                                 .with(LocalTime.of(22, 0));
        contest.setStartPhase1(startPhase1);
        LocalDateTime startPhase2 = startPhase1.plusDays(2);
        contest.setStartPhase2(startPhase2);
        LocalDateTime startPhase3 = startPhase2.plusHours(12);
        contest.setStartPhase3(startPhase3);
        contest.setType(Type.valueOf(contestDto.getType().toUpperCase(Locale.ROOT)));
        return contest;
    }

    public ContestViewDto createContestViewDto(Contest contest) {
        ContestViewDto contestViewDto = new ContestViewDto();
        contestViewDto.setTitle(contest.getTitle());
        contestViewDto.setDescription(contest.getDescription());
        contestViewDto.setCategory(contest.getCategory()
                                          .getName());
        contestViewDto.setPhotoUrl(contest.getPhotoUrl());
        contestViewDto.setCreatorName(contest.getOrganizer()
                                             .getFirstName() + " " + contest.getOrganizer()
                                                                            .getLastName());
        contestViewDto.setStartPhase1(contest.getStartPhase1());
        contestViewDto.setStartPhase2(contest.getStartPhase2());
        contestViewDto.setStartPhase3(contest.getStartPhase3());
        contestViewDto.setType(contest.getType().toString());
        calculateTimeRemaining(contestViewDto, contest.getStartPhase3());
        return contestViewDto;
    }
    private void calculateTimeRemaining(ContestViewDto dto, LocalDateTime startPhase3) {
        Duration duration = Duration.between(LocalDateTime.now(), startPhase3);
        dto.setDaysRemaining(duration.toDays());
        dto.setHoursRemaining(duration.toHours() % 24);
        dto.setMinutesRemaining(duration.toMinutes() % 60);
        dto.setSecondsRemaining(duration.getSeconds() % 60);
    }

//    public ContestSummaryDTO createFinishedContestViewDto(Contest contest) {
//
//        ContestViewDto contestViewDto = createContestViewDto(contest);
//        ContestSummaryDTO contestSummaryDTO = new ContestSummaryDTO();
//        contestSummaryDTO.setContestViewDto(contestViewDto);
//
//        List<PhotoSubmission> submissions = contest.getSubmissions();
//        TreeSet<Integer> top3Scores = submissions.stream()
//                                                 .map(PhotoSubmission::getReviewScore)
//                                                 .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.reverseOrder())));
//
//        top3Scores = top3Scores.stream()
//                               .limit(3)
//                               .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.reverseOrder())));
//
//        TreeSet<Integer> finalTop3Scores = top3Scores;
//        TreeMap<Integer, List<String>> top3 = submissions.stream()
//                                                         .filter(submission -> finalTop3Scores.contains(submission.getReviewScore()))
//                                                         .collect(Collectors.groupingBy(
//                                                                 PhotoSubmission::getReviewScore,
//                                                                 () -> new TreeMap<>(Comparator.reverseOrder()),
//                                                                 Collectors.mapping(submission -> submission.getCreator()
//                                                                                                            .getFirstName() + " " + submission.getCreator()
//                                                                                                                                              .getLastName(), Collectors.toList())
//                                                         ));
//
//        List<PlaceDTO> places = new ArrayList<>();
//        int place = 1;
//        for (Integer score : top3.keySet()) {
//            PlaceDTO placeDTO = new PlaceDTO();
//            placeDTO.setPlace(place);
//            placeDTO.setUsers(top3.get(score));
//            places.add(placeDTO);
//            place++;
//        }
//
//        contestSummaryDTO.setPlaces(places);
//        return contestSummaryDTO;
//
//
//    }


    public ContestSummaryDTO createFinishedContestViewDto(Contest contest) {
        ContestViewDto contestViewDto = createContestViewDto(contest);
        ContestSummaryDTO contestSummaryDTO = new ContestSummaryDTO();
        contestSummaryDTO.setContestViewDto(contestViewDto);

        List<PhotoSubmission> submissions = contest.getSubmissions();
        TreeSet<Integer> top3Scores = submissions.stream()
                                                 .map(PhotoSubmission::getReviewScore)
                                                 .collect(Collectors.toCollection(() -> new TreeSet<Integer>(Comparator.reverseOrder())));

        top3Scores = top3Scores.stream()
                               .limit(3)
                               .collect(Collectors.toCollection(() -> new TreeSet<Integer>(Comparator.reverseOrder())));

        TreeSet<Integer> finalTop3Scores = top3Scores;
        TreeMap<Integer, List<String>> top3 = submissions.stream()
                                                         .filter(submission -> finalTop3Scores.contains(submission.getReviewScore()))
                                                         .collect(Collectors.groupingBy(
                                                                 PhotoSubmission::getReviewScore,
                                                                 () -> new TreeMap<Integer, List<String>>(Comparator.reverseOrder()),
                                                                 Collectors.mapping(submission -> submission.getCreator()
                                                                                                            .getFirstName() + " " + submission.getCreator()
                                                                                                                                              .getLastName(), Collectors.toList())
                                                         ));

        List<PlaceDTO> places = new ArrayList<>();
        int place = 1;
        for (Integer score : top3.keySet()) {
            PlaceDTO placeDTO = new PlaceDTO();
            placeDTO.setPlace(place);
            placeDTO.setUsers(top3.get(score));
            places.add(placeDTO);
            place++;
        }

        contestSummaryDTO.setPlaces(places);
        return contestSummaryDTO;
    }
}
