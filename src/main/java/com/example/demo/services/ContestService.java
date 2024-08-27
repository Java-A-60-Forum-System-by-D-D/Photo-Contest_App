package com.example.demo.services;
import com.example.demo.models.Contest;
import com.example.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface ContestService {
    Contest getContestById(long id);
    Contest createContest(Contest contest, User user);
    List<Contest> getAllContests();

    Optional<Contest> findByTitle(String title);
}
