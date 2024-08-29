package com.example.demo.controllers.rest;

import com.example.demo.models.User;
import com.example.demo.models.dto.RegisterUserDto;
import com.example.demo.models.dto.UpdateUserDTO;
import com.example.demo.models.dto.UserViewDto;
import com.example.demo.models.filtering.UserFilterOptions;
import com.example.demo.models.mappers.UserMapper;
import com.example.demo.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @GetMapping
    public List<UserViewDto> getUsers(@RequestParam(required = false) String username,
                                      @RequestParam(required = false) String firstName,
                                      @RequestParam(required = false) String lastName,
                                      @RequestParam(required = false) String sortBy,
                                      @RequestParam(required = false) String sortDirection) {

        UserFilterOptions userFilterOptions = new UserFilterOptions(username, firstName, lastName, sortBy, sortDirection);
        List<UserViewDto> users = userService.getUsers(userFilterOptions)
                                             .stream()
                                             .map(userMapper::mapUserToUserViewDto)
                                             .toList();

        return users;
    }


    @PutMapping
    @RequestMapping("/{id}")
    public UserViewDto updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO updateUserDTO) {

        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        User loggedUser = userService.getUserByEmail(authentication.getName());
        User userToUpdate = userService.getUserById(id);

        if (!userToUpdate.getEmail()
                         .equalsIgnoreCase(loggedUser.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own profile");
        }

        userToUpdate = userMapper.updateUserFromDto(updateUserDTO, userToUpdate);


        return userMapper.mapUserToUserViewDto(userService.updateUser(userToUpdate));


    }
}
