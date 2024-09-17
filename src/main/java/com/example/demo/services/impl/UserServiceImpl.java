package com.example.demo.services.impl;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Contest;
import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.models.filtering.OptionalUserFilteringOptions;
import com.example.demo.models.filtering.UserFilterOptions;
import com.example.demo.models.filtering.UserSpecification;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {


    public static final String USER_NOT_FOUND = "User not found: ";
    public static final int MAX_POINTS_FOR_JUNKIE = 51;
    public static final int MAX_POINTS_FOR_ENTHUSIAST = 100;
    public static final int MAX_POINTS_FOR_MASTER = 151;
    private final UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUsersByEmail(email)
                                  .stream()
                                  .findFirst()
                                  .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return user;
    }

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id)
                             .orElse(null);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUsersByEmail(email)
                             .stream()
                             .findFirst()
                             .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND + email));
    }



    @Override
    public List<User> findUsersByRoleOrganizer(String role) {
        return userRepository.findUsersByRole(UserRole.ORGANIZER);
    }

    @Override
    public List<User> getUsers(UserFilterOptions userFilterOptions) {
        Specification<User> spec = UserSpecification.filterByOptions(userFilterOptions);
        return userRepository.findAll(spec);
    }

    @Override
    public User updateUser(User userToUpdate) {

        return userRepository.save(userToUpdate);
    }

    @Override
    public User calculateLevel(User user) {
        int score = user.getTotalScore();

        if (score < MAX_POINTS_FOR_JUNKIE) {
            user.setRole(UserRole.JUNKIE);
        } else if (score < MAX_POINTS_FOR_ENTHUSIAST) {
            user.setRole(UserRole.ENTHUSIAST);
        } else if (score < MAX_POINTS_FOR_MASTER) {
            user.setRole(UserRole.MASTER);
        } else {
            user.setRole(UserRole.DICTATOR);
        }

        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                             .stream()
                             .findFirst()
                             .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND + username));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



    @Override
    public List<User> getUsersByJurorContests(Contest contest) {
        return userRepository.findUsersByJurorContests(contest.getId());
    }

    @Override
    public List<User> getUsersByRole() {
        return userRepository.findParticipantUsers();
    }

    @Override
    public List<String> findUsernamesByTerm(String term) {
        return userRepository.findEmailByTerm(term);
    }

    @Override
    public User updateFirstName(long userId, String firstName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setFirstName(firstName);
        return userRepository.save(user);
    }

    @Override
    public User updateLastName(long userId, String lastName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setLastName(lastName);
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsersByOptions(OptionalUserFilteringOptions userFilterOptions) {
        return userRepository.findAllByOptions( userFilterOptions.getEmail(),userFilterOptions.getFirstName(),userFilterOptions.getLastName(), userFilterOptions.getRole());
    }




}


