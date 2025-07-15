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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserServiceFactory userServiceFactory;
    private final UserServiceDtoFactory userServiceDtoFactory;

    @Override
    public UserResponse createUser(UserRequest request) {

        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new UserAlreadyExistException();
                });

        var user = userServiceFactory.from(request);
        var token = jwtUtil.generateToken(user.getEmail());
        user.setToken(token);
        var savedUser = userRepository.save(user);

        return this.userServiceDtoFactory.toCreateResponse(savedUser);
    }

    @Override
    public UserResponse login(String authHeader) {

        var token = authHeader.replace("Bearer ", "");
        var email = jwtUtil.getUsernameFromToken(token);
        var user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        user.setLastLogin(LocalDateTime.now());
        var newToken = jwtUtil.generateToken(email);
        user.setToken(newToken);

        userRepository.save(user);
        return this.userServiceDtoFactory.toLoginResponse(user);
    }
}