package com.example.demo.services.impl;

import com.example.demo.exceptions.AuthorizationUserException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Contest;
import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.repositories.ContestRepository;
import com.example.demo.services.ContestService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContestServiceImpl implements ContestService {
    public static final String CONTEST_NOT_FOUND = "Contest %d not found";
    public static final String USER_IS_NOT_AUTHORIZED_TO_CREATE_CONTEST = "User is not authorized to create contest";
    private final ContestRepository contestRepository;

    public ContestServiceImpl(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }

    @Override
    public Contest getContestById(long id) {
        return contestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format(CONTEST_NOT_FOUND, id)));
    }

    @Override
    public Contest createContest(Contest contest, User user) {
        if(!user.getRole().equals(UserRole.ORGANIZER)){
            throw new AuthorizationUserException(USER_IS_NOT_AUTHORIZED_TO_CREATE_CONTEST);
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
}
