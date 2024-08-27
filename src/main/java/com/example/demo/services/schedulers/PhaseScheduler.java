package com.example.demo.services.schedulers;


import com.example.demo.models.Contest;
import com.example.demo.models.Phase;
import com.example.demo.repositories.ContestRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PhaseScheduler {

    private final ContestRepository contestRepository;

    public PhaseScheduler(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }

    @Scheduled(cron = "0 0 22 * * ?") // Run every night at 10 PM
    public void startPhase2() {
        LocalDateTime now = LocalDateTime.now();
        List<Contest> contests = contestRepository.findAll();

        for (Contest contest : contests) {
            if (contest.getStartPhase1().isBefore(now) && contest.getPhase().equals(Phase.NOT_STARTED)) {
                contest.setPhase(Phase.PHASE_1);
                contest.setUpdatedAt(now);
                contestRepository.save(contest);
            } else if (contest.getStartPhase2().isBefore(now) && !contest.getPhase().equals(Phase.PHASE_2)) {
                contest.setPhase(Phase.PHASE_2);
                contest.setUpdatedAt(now);
                contestRepository.save(contest);
            } else if (contest.getStartPhase3().isBefore(now) && !contest.getPhase().equals(Phase.FINISHED)) {
                contest.setPhase(Phase.FINISHED);
                contest.setUpdatedAt(now);
                contestRepository.save(contest);
            }
        }
    }
}