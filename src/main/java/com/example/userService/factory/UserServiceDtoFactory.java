package com.example.userService.factory;

import com.example.userService.dto.UserResponse;
import com.example.userService.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Factory for creating {@link UserResponse} DTOs from {@link User} entities.
 * <p>
 * Used to decouple internal entity models from the data returned to clients.
 */
@Component
@RequiredArgsConstructor
public class UserServiceDtoFactory {

    /**
     * Converts a {@link User} entity into a {@link UserResponse} for the user creation operation.
     * <p>
     * This response excludes sensitive information such as name, email, password, and phone list.
     *
     * @param user the user entity to convert
     * @return a {@link UserResponse} with basic metadata
     */
    public UserResponse toCreateResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .created(user.getCreated())
                .lastLogin(user.getLastLogin())
                .token(user.getToken())
                .isActive(user.isActive())
                .build();
    }
    /**
     * Converts a {@link User} entity into a detailed {@link UserResponse} for login operations.
     * <p>
     * This includes sensitive fields like name, email, password, and phone list,
     * typically returned after a successful authentication.
     *
     * @param user the user entity to convert
     * @return a full {@link UserResponse} including user profile and contact details
     */
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
