package com.example.userService.service;

import com.example.userService.dto.UserRequest;
import com.example.userService.dto.UserResponse;
import com.example.userService.exceptions.UserAlreadyExistException;
import com.example.userService.exceptions.UserNotFoundException;
import com.example.userService.repository.UserRepository;
import com.example.userService.security.JwtUtil;
import com.example.userService.factory.UserServiceDtoFactory;
import com.example.userService.factory.UserServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utils.JwtHelper;

import java.time.LocalDateTime;

/**
 * Implementation of the {@link IUserService} interface.
 * <p>
 * Handles user creation and login logic, including:
 * <ul>
 *     <li>Validating existing users</li>
 *     <li>Generating and assigning JWT tokens</li>
 *     <li>Mapping DTOs using factories</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    /** Repository for interacting with user persistence. */
    private final UserRepository userRepository;
    /** Utility for generating and parsing JWT tokens. */
    private final JwtUtil jwtUtil;
    /** Factory for creating User entities from request DTOs. */
    private final UserServiceFactory userServiceFactory;
    /** Factory for converting User entities into response DTOs. */
    private final UserServiceDtoFactory userServiceDtoFactory;

    /**
     * Creates a new user in the system.
     * <p>
     * - Validates that the user does not already exist.<br>
     * - Converts request to entity using factory.<br>
     * - Generates a JWT token and saves the user.<br>
     * - Returns a DTO with relevant response information.
     *
     * @param request the incoming user registration data
     * @return a {@link UserResponse} containing created user info
     * @throws UserAlreadyExistException if the user already exists by email
     */
    @Override
    public UserResponse createUser(UserRequest request) {

        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new UserAlreadyExistException();
                });

        var user = userServiceFactory.from(request);
        var token = jwtUtil.generateToken(user.getEmail());
        user.setToken(token);
        userRepository.save(user);

        return this.userServiceDtoFactory.toCreateResponse(user);
    }

    /**
     * Authenticates a user using a JWT token found in the Authorization header.
     * <p>
     * - Extracts the token from the header.<br>
     * - Validates the token and retrieves the associated user.<br>
     * - Updates last login and issues a new token.<br>
     * - Returns a login response DTO.
     *
     * @param authHeader the HTTP Authorization header with "Bearer &lt;token&gt;"
     * @return a {@link UserResponse} with updated login info and new token
     * @throws UserNotFoundException if the user is not found
     */
    @Override
    public UserResponse login(String authHeader) {

        var email = jwtUtil.getUsernameFromToken(JwtHelper.getTokenFromBearer(authHeader));
        var user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        user.setLastLogin(LocalDateTime.now());
        user.setToken(jwtUtil.generateToken(email));

        userRepository.save(user);
        return this.userServiceDtoFactory.toLoginResponse(user);
    }
}