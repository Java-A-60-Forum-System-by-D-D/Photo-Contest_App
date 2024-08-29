package com.example.demo.models.mappers;

import com.example.demo.models.AuthUser;
import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.models.dto.LoginUserDto;
import com.example.demo.models.dto.RegisterUserDto;
import com.example.demo.models.dto.UpdateUserDTO;
import com.example.demo.models.dto.UserViewDto;
import com.example.demo.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserMapper(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUserFromDto(RegisterUserDto registerUserDto) {
        User user = new User();
        user.setUsername(registerUserDto.getUsername());
        user.setEmail(registerUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        user.setFirstName(registerUserDto.getFirstName());
        user.setLastName(registerUserDto.getLastName());
        user.setRole(UserRole.JUNKIE);
        return user;
    }

    public UserViewDto mapUserToUserViewDto(User user) {
        UserViewDto userViewDto = new UserViewDto();
        userViewDto.setUsername(user.getUsername());
        userViewDto.setEmail(user.getEmail());
        userViewDto.setFirstName(user.getFirstName());
        userViewDto.setLastName(user.getLastName());
        return userViewDto;
    }

    public User updateUserFromDto(UpdateUserDTO updateUserDTO, User userToUpdate) {

        userToUpdate.setFirstName(updateUserDTO.getFirstName());
        userToUpdate.setLastName(updateUserDTO.getLastName());
        userToUpdate.setUsername(updateUserDTO.getUsername());
        userToUpdate.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));

        return userToUpdate;
    }

    public AuthUser mapLoginUserDtoToAuthUser(LoginUserDto loginUserDto) {
        AuthUser user = userService.getUserByEmail(loginUserDto.getEmail());
        return user;

    }


}
