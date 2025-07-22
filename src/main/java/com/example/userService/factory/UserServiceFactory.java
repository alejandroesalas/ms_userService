package com.example.userService.factory;

import com.example.userService.dto.UserRequest;
import com.example.userService.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Factory class responsible for constructing {@link User} entities from {@link UserRequest} DTOs.
 * <p>
 * This factory encapsulates the logic of entity initialization, including ID generation,
 * password encoding, default timestamps, and phone list transformation.
 */
@Component
@RequiredArgsConstructor
public class UserServiceFactory {

    private final PasswordEncoder encoder;

    /**
     * Converts a {@link UserRequest} DTO into a fully populated {@link User} entity.
     * <p>
     * This method:
     * <ul>
     *     <li>Generates a unique ID for the user</li>
     *     <li>Encodes the raw password using the configured {@link PasswordEncoder}</li>
     *     <li>Sets creation and last login timestamps to the current time</li>
     *     <li>Initializes the account as active</li>
     *     <li>Maps the list of phone DTOs to phone entities using {@link PhoneServiceFactory}</li>
     * </ul>
     *
     * @param request the user input data coming from the API
     * @return a new {@link User} entity ready to be persisted
     */
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
