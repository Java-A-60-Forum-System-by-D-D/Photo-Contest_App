package com.example.demo.services.impl;

import com.example.demo.exceptions.AuthorizationUserException;
import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.*;
import com.example.demo.models.filtering.ContestFilterOptions;
import com.example.demo.repositories.ContestRepository;
import com.example.demo.services.ContestService;
import com.example.demo.services.NotificationService;
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
    public static final String INVITATIONAL_MESSAGE = "You have been invited to participate in a contest. You can see details in Your Contest Menu";
    private final ContestRepository contestRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final EmailService emailService;


    public ContestServiceImpl(ContestRepository contestRepository, UserService userService, NotificationService notificationService, EmailService emailService) {
        this.contestRepository = contestRepository;
        this.userService = userService;
        this.notificationService = notificationService;
        this.emailService = emailService;
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
            notificationService.sendNotification("New contest has been created", NotificationType.JURY_INVITATION, u);
        });
        List<User> participants = userService.getUsersByRole();
        participants.forEach(u -> {
            emailService.sendEmail(u.getEmail(), "New contest has been created", String.format("New contest %s has been created", savedContest.getTitle()));
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
        notificationService.sendNotification("You have been invited to be a jury in a contest", NotificationType.JURY_INVITATION, juryToAdd);
        emailService.sendEmail(juryToAdd.getEmail(), "Invitation to be a jury in a contest.", String.format("You have been invited to be a jury in the contest %s", contest.getTitle()));
        userService.save(juryToAdd);


        return contest;
    }

    @Override
    public List<Contest> getAllContestsByOptions(ContestFilterOptions filterOptions) {
        return contestRepository.findAllByFilterOptions(filterOptions.getTitle(), filterOptions.getCategory(), filterOptions.getType(), filterOptions.getPhase(), filterOptions.getUsername());
    }

    @Override
    public List<Contest> getContestByCategoryName(Category category) {
        List<Contest> contests = contestRepository.findContestByCategory_Id(category.getId());
        if (contests.isEmpty()) {
            System.out.println("List is empty");
            throw new EntityNotFoundException("CANT FIND CONTESTS");
        }
        return contests;
    }

    @Override
    public List<Contest> getPhaseOneContests() {
        return contestRepository.findAllByPhase_Phase1();
    }

    @Override
    public List<Contest> getPhaseTwoContests() {
        return contestRepository.findAllByPhase_Phase2();
    }

    @Override
    public List<Contest> geFinishedContests() {
        return contestRepository.findAllByPhase_Finished();
    }

    @Override
    public Contest inviteUserToContest(long id, User userToInvite, User user) {
        Contest contest = getContestById(id);
        List<Contest> organizedContests = user.getOrganizedContests();
        UserRole userToInviteRole = userToInvite.getRole();
        UserRole userRole = user.getRole();

        if (!userRole.equals(UserRole.ORGANIZER)) {
            throw new AuthorizationUserException("User is not organizer");
        } else if (!organizedContests
                .contains(contest)) {
            throw new AuthorizationUserException("User is not the organizer of this contest");
        } else if (userToInviteRole
                .equals(UserRole.ORGANIZER)) {
            throw new AuthorizationUserException("The user you want to invite is not a photographer");
        } else if (userToInvite.getParticipatedContests()
                .contains(contest)) {
            throw new EntityDuplicateException("The user is already invited to this contest");
        }
//        userToInvite.getInvitedContests()
//                    .add(contest);
//        userService.saveUser(userToInvite);
        contest.getInvitedUsers()
                .add(userToInvite);
//        emailService.sendEmail(userToInvite.getEmail(), "Invitation to contest", String.format("You have been invited to participate in the contest %s", contest.getTitle()));
        contestRepository.save(contest);
        notificationService.sendNotification(INVITATIONAL_MESSAGE, NotificationType.PARTICIPATION_REMINDER, userToInvite);
        return contest;

    }

    @Override
    public List<Contest> getPhaseOneContestsAndTypeOpen() {
        return contestRepository.findAllByPhase_Phase1_AndType_Open();
    }

    @Override
    public List<Contest> getNotStartedContests() {
        return contestRepository.findContestByPhase_NotStarted();
    }

    @Override
    public List<String> getAllPhotos() {
        return contestRepository.getAllByPhotos();
    }

    @Override
    public void deleteContest(long id) {
        Contest contest = getContestById(id);

        List<User> juryList = contest.getJurorContests();

        juryList.stream()
                .forEach(jury -> {
                    jury.getJurorContests()
                            .remove(contest);
                    userService.save(jury);
                });


        List<User> invitedUsers = contest.getInvitedUsers();

        if (invitedUsers != null) {
            invitedUsers.stream()
                    .forEach(invitedUser -> {
                        invitedUser.getParticipatedContests()
                                .remove(contest);
                        userService.save(invitedUser);
                    });
        }

        List<User> participants = contest.getParticipants();

        if (participants != null) {
            participants.stream()
                    .forEach(participant -> {
                        participant.getParticipatedContests()
                                .remove(contest);
                        userService.save(participant);
                    });
        }

        contestRepository.delete(contest);


    }

    @Override
    public List<Contest> getInvitedContests(User user) {
        return contestRepository.findContestsByInvitedUsersEqualsUserId(user.getId());
    }


    @Override
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

    @Override
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
