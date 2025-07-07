package com.example.userService.service;

import com.example.userService.dto.UserRequest;
import com.example.userService.dto.UserResponse;
import com.example.userService.entity.User;
import com.example.userService.exceptions.UserAlreadyExistException;
import com.example.userService.exceptions.UserNotFoundException;
import com.example.userService.repository.UserRepository;
import com.example.userService.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;

    @Override
    public UserResponse createUser(UserRequest request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    throw new UserAlreadyExistException();
                });

        var user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setCreated(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setActive(true);
        user.setPhones(request.getPhones());

        var token = jwtUtil.generateToken(user.getEmail());
        user.setToken(token);

        userRepository.save(user);

        return mapToResponse(user);
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
        return mapToResponse(user);
    }

    public UserResponse mapToResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .created(user.getCreated())
                .lastLogin(user.getLastLogin())
                .token(user.getToken())
                .isActive(user.isActive())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phones(user.getPhones())
                .build();
    }
}