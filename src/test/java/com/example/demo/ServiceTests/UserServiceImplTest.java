package com.example.demo.ServiceTests;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Contest;
import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.models.filtering.OptionalUserFilteringOptions;
import com.example.demo.models.filtering.UserFilterOptions;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;



    @Test
    void loadUserByUsernameReturnsUserDetailsWhenUserExists() {
        User user = new User();
        when(userRepository.findUsersByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("test@example.com");

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void loadUserByUsernameThrowsExceptionWhenUserDoesNotExist() {
        when(userRepository.findUsersByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("test@example.com"));
    }

    @Test
    void getAllUsersReturnsAllUsers() {
        User user = new User();
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(user, result.get(0));
    }

    @Test
    void getUsersByJurorContestsReturnsUsers() {
        Contest contest = new Contest();
        User user = new User();
        when(userRepository.findUsersByJurorContests(contest.getId())).thenReturn(List.of(user));

        List<User> result = userService.getUsersByJurorContests(contest);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(user, result.get(0));
    }

    @Test
    void getUsersByRoleReturnsParticipantUsers() {
        User user = new User();
        when(userRepository.findParticipantUsers()).thenReturn(List.of(user));

        List<User> result = userService.getUsersByRole();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(user, result.get(0));
    }

    @Test
    void findUsernamesByTermReturnsUsernames() {
        String term = "test";
        String email = "test@example.com";
        when(userRepository.findEmailByTerm(term)).thenReturn(List.of(email));

        List<String> result = userService.findUsernamesByTerm(term);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(email, result.get(0));
    }

    @Test
    void getUsersByOptionsReturnsFilteredUsers() {
        OptionalUserFilteringOptions options = new OptionalUserFilteringOptions();
        User user = new User();
        when(userRepository.findAllByOptions(any(), any(), any(), any())).thenReturn(List.of(user));

        List<User> result = userService.getUsersByOptions(options);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(user, result.get(0));
    }


    @Test
    void findUsersByRoleOrganizerReturnsUsers() {
        User user = new User();
        when(userRepository.findUsersByRole(UserRole.ORGANIZER)).thenReturn(List.of(user));

        List<User> result = userService.findUsersByRoleOrganizer("ORGANIZER");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(user, result.get(0));
    }

    @Test
    void getUsersReturnsFilteredUsers() {
        UserFilterOptions options = new UserFilterOptions();
        User user = new User();
        when(userRepository.findAll(any(Specification.class))).thenReturn(List.of(user));

        List<User> result = userService.getUsers(options);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(user, result.get(0));
    }

    @Test
    void updateUserUpdatesAndPersistsUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(user);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void calculateLevelAssignsCorrectRoleBasedOnScore() {
        User user = new User();
        user.setTotalScore(120);

        User result = userService.calculateLevel(user);

        assertEquals(UserRole.MASTER, result.getRole());
    }

    @Test
    void calculateLevelAssignsJunkieRoleForLowScore() {
        User user = new User();
        user.setTotalScore(50);

        User result = userService.calculateLevel(user);

        assertEquals(UserRole.JUNKIE, result.getRole());
    }

    @Test
    void calculateLevelAssignsEnthusiastRoleForMediumScore() {
        User user = new User();
        user.setTotalScore(75);

        User result = userService.calculateLevel(user);

        assertEquals(UserRole.ENTHUSIAST, result.getRole());
    }

    @Test
    void calculateLevelAssignsDictatorRoleForHighScore() {
        User user = new User();
        user.setTotalScore(200);

        User result = userService.calculateLevel(user);

        assertEquals(UserRole.DICTATOR, result.getRole());
    }



    /////////////////////////////


    @Test
    void getUserByIdReturnsUserWhenUserExists() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void getUserByIdReturnsNullWhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.getUserById(1L);

        assertNull(result);
    }

    @Test
    void saveUserPersistsUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.saveUser(user);

        assertNotNull(result);
        assertEquals(user, result);
    }
//
@Test
void getUserByEmailReturnsUserWhenUserExists() {
    User user = new User();
    when(userRepository.findUsersByEmail("test@example.com")).thenReturn(Optional.of(user));

    User result = userService.getUserByEmail("test@example.com");

    assertNotNull(result);
    assertEquals(user, result);
}

    @Test
    void getUserByEmailThrowsExceptionWhenUserDoesNotExist() {
        when(userRepository.findUsersByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserByEmail("test@example.com"));
    }


    @Test
    void getUserByUsernameReturnsUserWhenUserExists() {
        User user = new User();
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.findUserByUsername("username")).thenReturn(Optional.ofNullable(users.get(0)));

        User result = userService.getUserByUsername("username");

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void getUserByUsernameThrowsExceptionWhenUserDoesNotExist() {
        when(userRepository.findUserByUsername("username")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserByUsername("username"));
    }

    @Test
    void updateFirstNameUpdatesAndPersistsUser() {
        User user = new User();
        user.setFirstName("OldFirstName");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateFirstName(1L, "NewFirstName");

        assertNotNull(result);
        assertEquals("NewFirstName", result.getFirstName());
        verify(userRepository).save(user);
    }

    @Test
    void updateLastNameUpdatesAndPersistsUser() {
        User user = new User();
        user.setLastName("OldLastName");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateLastName(1L, "NewLastName");

        assertNotNull(result);
        assertEquals("NewLastName", result.getLastName());
        verify(userRepository).save(user);
    }
}