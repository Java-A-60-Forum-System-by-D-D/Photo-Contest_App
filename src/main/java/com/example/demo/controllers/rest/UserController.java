package com.example.demo.controllers.rest;

import com.example.demo.models.User;
import com.example.demo.models.dto.UserViewDto;
import com.example.demo.models.filtering.UserFilterOptions;
import com.example.demo.models.mappers.UserMapper;
import com.example.demo.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
