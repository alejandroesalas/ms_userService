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
        userRepository.save(user);

        return this.userServiceDtoFactory.toCreateResponse(user);
    }

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