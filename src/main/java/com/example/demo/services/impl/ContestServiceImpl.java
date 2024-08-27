package com.example.demo.services.impl;

import com.example.demo.exceptions.AuthorizationUserException;
import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Contest;
import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.models.dto.ContestViewDto;
import com.example.demo.repositories.ContestRepository;
import com.example.demo.services.ContestService;
import com.example.demo.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ContestServiceImpl implements ContestService {
    public static final String CONTEST_NOT_FOUND = "Contest %d not found";
    public static final String USER_IS_NOT_AUTHORIZED_TO_CREATE_CONTEST = "User is not authorized to create contest";
    public static final String CONTEST_WITH_SUCH_TITLE_ALREADY_EXISTS = "Contest with such title already exists";
    private final ContestRepository contestRepository;


    public ContestServiceImpl(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
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

        return contestRepository.save(contest);
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
}
