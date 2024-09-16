package com.example.demo.services.schedulers;


import com.example.demo.models.*;
import com.example.demo.repositories.ContestRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.NotificationService;
import com.example.demo.services.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PhaseScheduler {

    private final ContestRepository contestRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    public PhaseScheduler(ContestRepository contestRepository, UserRepository userRepository, UserService userService, NotificationService notificationService) {
        this.contestRepository = contestRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

//    @Scheduled(cron = "0 0 22 * * ?") // Run every night at 10 PM
    @Scheduled(cron = "0 0/30 * * * ?") // Run every 30 minutes
    public void startPhase2() {
        LocalDateTime now = LocalDateTime.now();

        List<Contest> unFinishedContests = contestRepository.findAllUnfinishedContests();

        for (Contest contest : unFinishedContests) {
            if (contest.getStartPhase1()
                       .isBefore(now) && contest.getPhase()
                                                .equals(Phase.NOT_STARTED)) {
                contest.setPhase(Phase.PHASE_1);
                contest.setUpdatedAt(now);
                notificationService.sendNotification("Phase 1 has started for contest " + contest.getTitle(), NotificationType.CONTEST_UPDATE, contest.getOrganizer());
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