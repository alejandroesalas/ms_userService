package com.example.userService.factory;

import com.example.userService.dto.UserRequest;
import com.example.userService.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserServiceFactory {

    private final PasswordEncoder encoder;

    public User from(UserRequest request){

        return User.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .name(request.getName())
                .created(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .isActive(Boolean.TRUE)
                .phones(PhoneServiceFactory.fromDtoList(request.getPhones()))
                .build();
    }
}
