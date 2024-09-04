package com.example.demo.services.impl;

import com.example.demo.exceptions.AuthorizationUserException;
import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.*;
import com.example.demo.models.filtering.ContestFilterOptions;
import com.example.demo.repositories.ContestRepository;
import com.example.demo.services.ContestService;
import com.example.demo.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContestServiceImpl implements ContestService {
    public static final String CONTEST_NOT_FOUND = "Contest %d not found";
    public static final String USER_IS_NOT_AUTHORIZED_TO_CREATE_CONTEST = "User is not authorized to create contest";
    public static final String CONTEST_WITH_SUCH_TITLE_ALREADY_EXISTS = "Contest with such title already exists";
    private final ContestRepository contestRepository;
    private final UserService userService;


    public ContestServiceImpl(ContestRepository contestRepository, UserService userService) {
        this.contestRepository = contestRepository;
        this.userService = userService;
    }

    @Override
    public Contest getContestById(long id) {
        return contestRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(String.format(CONTEST_NOT_FOUND, id)));
    }

    @Override
    @Transactional
    public Contest createContest(Contest contest, User user) {
        if (!user.getRole()
                 .equals(UserRole.ORGANIZER)) {
            throw new AuthorizationUserException(USER_IS_NOT_AUTHORIZED_TO_CREATE_CONTEST);
        }
        if (contestRepository.findByTitle(contest.getTitle())
                             .isPresent()) {
            throw new EntityDuplicateException(CONTEST_WITH_SUCH_TITLE_ALREADY_EXISTS);
        }

        Contest savedContest = contestRepository.save(contest);

        List<User> organizers = userService.findUsersByRoleOrganizer("ORGANIZER");
        organizers.forEach(u -> {
            u.getJurorContests()
             .add(savedContest);
            userService.save(u);
        });

        return savedContest;
    }


    @Override
    public List<Contest> getAllContests() {
        return List.of();
    }

    @Override
    public Optional<Contest> findByTitle(String title) {
        return contestRepository.findByTitle(title);
    }

    @Override
    public List<Contest> getOpenContests() {
        return contestRepository.findAllByPhase_Phase1();
    }

    @Override
    public List<Contest> getAllParticipatedContests(User user) {
        return contestRepository.findAllParticipated(user.getId());
    }

    @Override
    public List<Contest> getFinishedContests(User user) {
        return contestRepository.findAllFinished(user.getId());
    }

    @Override
    public boolean checkIfUserHasAlreadySubmittedPhoto(User user, Contest contest) {
        return contestRepository.findAllParticipated(user.getId())
                                .contains(contest);
    }


    @Override
    public void save(Contest contest) {
        contestRepository.save(contest);
    }

    @Override
    @Transactional
    public Contest addJury(long contestId, User juryToAdd, User user) {

        Contest contest = getContestById(contestId);
        List<Contest> organizedContests = user.getOrganizedContests();
        UserRole juryRole = juryToAdd.getRole();
        UserRole userRole = user.getRole();

        if (!userRole.equals(UserRole.ORGANIZER)) {
            throw new AuthorizationUserException("User is not organizer");
        } else if (!organizedContests
                .contains(contest)) {
            throw new AuthorizationUserException("User is not the organizer of this contest");
        } else if (!juryRole
                .equals(UserRole.MASTER) && !juryRole
                .equals(UserRole.DICTATOR)) {
            throw new AuthorizationUserException("The user you want to add as a jury does not have the desired level");
        } else if (juryToAdd.getJurorContests()
                            .contains(contest)) {
            throw new EntityDuplicateException("The user is already invited as a jury to this contest");
        }


        juryToAdd.getJurorContests()
                 .add(contest);

        juryToAdd.setTotalScore(juryToAdd.getTotalScore() + 3);
        userService.calculateLevel(juryToAdd);

        userService.save(juryToAdd);


        return contest;
    }

    @Override
    public List<Contest> getAllContestsByOptions(ContestFilterOptions filterOptions) {
        return contestRepository.findAllByFilterOptions(filterOptions.getTitle(), filterOptions.getCategory(), filterOptions.getType(), filterOptions.getPhase(), filterOptions.getUsername());
    }

    @Override
    public List<Contest> getContestByCategoryName(Category category) {
        List<Contest> contests = contestRepository.getFinishedContestsByCategory(category.id);
        if (contests.isEmpty()) {
            System.out.println("List is empty");
            throw new EntityNotFoundException("CANT FIND CONTESTS");
        }
        return contests;
    }


    public TreeMap<Integer, List<User>> calculateFinalContestPoints(List<PhotoSubmission> submissions, List<User> users) {

        TreeSet<Integer> top3Scores = submissions.stream()
                                                 .map(PhotoSubmission::getReviewScore)
                                                 .collect(Collectors.toCollection(() -> new TreeSet<Integer>(Comparator.reverseOrder())));

        TreeSet<Integer> finalTop3Scores = top3Scores.stream()
                                                     .limit(3)
                                                     .collect(Collectors.toCollection(TreeSet::new));

        TreeMap<Integer, List<User>> top3 = submissions.stream()
                                                       .filter(submission -> finalTop3Scores.contains(submission.getReviewScore()))
                                                       .collect(Collectors.groupingBy(
                                                               PhotoSubmission::getReviewScore,
                                                               () -> new TreeMap<>(Comparator.reverseOrder()),
                                                               Collectors.mapping(PhotoSubmission::getCreator, Collectors.toList())
                                                       ));

        ranking(top3);

        return top3;


    }


    public void ranking(TreeMap<Integer, List<User>> top3) {

        List<Integer> scores = new ArrayList<>(top3.keySet());
        if (scores.size() > 0) {
            List<User> highestUsers = top3.get(scores.get(0));
            if (highestUsers.size() == 1) {
                highestUsers.get(0)
                            .setTotalScore(highestUsers.get(0)
                                                       .getTotalScore() + 50);
            } else {
                highestUsers.forEach(user -> user.setTotalScore(user.getTotalScore() + 40));
            }
        }

        if (scores.size() > 1) {
            List<User> secondHighestUsers = top3.get(scores.get(1));
            if (secondHighestUsers.size() == 1) {
                secondHighestUsers.get(0)
                                  .setTotalScore(secondHighestUsers.get(0)
                                                                   .getTotalScore() + 35);
            } else {
                secondHighestUsers.forEach(user -> user.setTotalScore(user.getTotalScore() + 25));
            }
        }

        if (scores.size() > 2) {
            List<User> thirdHighestUsers = top3.get(scores.get(2));
            if (thirdHighestUsers.size() == 1) {
                thirdHighestUsers.get(0)
                                 .setTotalScore(thirdHighestUsers.get(0)
                                                                 .getTotalScore() + 20);
            } else {
                thirdHighestUsers.forEach(user -> user.setTotalScore(user.getTotalScore() + 10));
            }
        }
    }


}
