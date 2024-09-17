package com.example.demo.services.schedulers;


import com.example.demo.models.*;
import com.example.demo.repositories.ContestRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.NotificationService;
import com.example.demo.services.UserService;
import com.example.demo.services.impl.EmailService;
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
    private final EmailService emailService;

    public PhaseScheduler(ContestRepository contestRepository, UserRepository userRepository, UserService userService, NotificationService notificationService, EmailService emailService) {
        this.contestRepository = contestRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.notificationService = notificationService;
        this.emailService = emailService;
    }
//
    @Scheduled(cron = "0 0 22 * * ?") // Run every night at 10 PM
//    @Scheduled(cron = "0 0/1 * * * ?") // Run every 30 minutes
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
                contest.getJurorContests()
                       .stream()
                       .forEach(user -> emailService.sendEmail(user.getEmail(), "Phase 2 has started for contest " + contest.getTitle(), "You can view all submissions into your Jury Panel"));
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
                                            notificationService.sendNotification("You have received " + reviewScore + " points for your submission in contest " + contest.getTitle(), NotificationType.CONTEST_UPDATE, user);
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