package com.example.demo.services.schedulers;


import com.example.demo.models.Contest;
import com.example.demo.models.Phase;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.repositories.ContestRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.example.demo.models.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PhaseScheduler {

    private final ContestRepository contestRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public PhaseScheduler(ContestRepository contestRepository, UserRepository userRepository, UserService userService) {
        this.contestRepository = contestRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 22 * * ?") // Run every night at 10 PM
    public void startPhase2() {
        LocalDateTime now = LocalDateTime.now();
        //todo, list all contest that are not in Phase.Finished
        List<Contest> contests = contestRepository.findAll();

        for (Contest contest : contests) {
            if (contest.getStartPhase1()
                       .isBefore(now) && contest.getPhase()
                                                .equals(Phase.NOT_STARTED)) {
                contest.setPhase(Phase.PHASE_1);
                contest.setUpdatedAt(now);
                contestRepository.save(contest);
            } else if (contest.getStartPhase2()
                              .isBefore(now) && !contest.getPhase()
                                                        .equals(Phase.PHASE_2)) {
                contest.setPhase(Phase.PHASE_2);
                contest.setUpdatedAt(now);
                contestRepository.save(contest);
            } else if (contest.getStartPhase3()
                              .isBefore(now) && !contest.getPhase()
                                                        .equals(Phase.FINISHED)) {
                contest.setPhase(Phase.FINISHED);
                contest.setUpdatedAt(now);

                List<PhotoSubmission> submissions = contest.getSubmissions();
                List<User> users = submissions.stream()
                                              .map(PhotoSubmission::getCreator)
                                              .toList();
                submissions.stream()
                           .forEach(submission -> {
                               int reviewScore = submission.getReviewScore();
                               users.stream()
                                    .forEach(user -> {
                                        if (user.equals(submission.getCreator())) {
                                            user.setTotalScore(user.getTotalScore() + reviewScore);
                                            userService.calculateLevel(user);
                                        }
                                    });

                           });



                userRepository.saveAll(users);

                contestRepository.save(contest);
            }
        }
    }
}