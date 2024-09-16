package com.example.demo.controllers.rest;

import com.example.demo.models.Phase;
import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.models.dto.RegisterUserDto;
import com.example.demo.models.dto.UpdateUserDTO;
import com.example.demo.models.dto.UserViewDto;
import com.example.demo.models.filtering.UserFilterOptions;
import com.example.demo.models.mappers.UserMapper;
import com.example.demo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "users", description = "User operations")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @Operation(summary = "View a list of available users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<UserViewDto> getUsers(@Parameter(description = "Email to filter users by", required = false) @RequestParam(required = false) String email,
                                      @Parameter(description = "First name to filter users by", required = false) @RequestParam(required = false) String firstName,
                                      @Parameter(description = "Last name to filter users by", required = false) @RequestParam(required = false) String lastName,
                                      @Parameter(description = "Field to sort users by", required = false) @RequestParam(required = false) String sortBy,
                                      @Parameter(description = "Direction to sort users by", required = false) @RequestParam(required = false) String sortDirection,
                                      @Parameter(description = "Role to filter users by", required = false) @RequestParam(required = false) String role) {
        UserRole userRole = null;
        if (userRole != null) {
            userRole = UserRole.valueOf(role.toUpperCase());
        }
        UserFilterOptions userFilterOptions = new UserFilterOptions(email, firstName, lastName, sortBy, sortDirection, userRole);
        List<UserViewDto> users = userService.getUsers(userFilterOptions)
                                             .stream()
                                             .map(userMapper::mapUserToUserViewDto)
                                             .toList();

        return users;
    }

    @Operation(summary = "Update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated user"),
            @ApiResponse(responseCode = "403", description = "You can only update your own profile"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public UserViewDto updateUser(@Parameter(description = "ID of the user to update", required = true) @PathVariable Long id,
                                  @Parameter(description = "Updated user information", required = true) @RequestBody UpdateUserDTO updateUserDTO){

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
