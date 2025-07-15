package com.example.userService.factory;

import com.example.userService.dto.UserResponse;
import com.example.userService.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserServiceDtoFactory {

    public UserResponse toCreateResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .created(user.getCreated())
                .lastLogin(user.getLastLogin())
                .token(user.getToken())
                .isActive(user.isActive())
                .build();
    }
    public UserResponse toLoginResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .created(user.getCreated())
                .lastLogin(user.getLastLogin())
                .token(user.getToken())
                .isActive(user.isActive())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneList(PhoneServiceFactory.toDtoList(user.getPhones()))
                .build();
    }
}
