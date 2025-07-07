package com.example.userService.service;

import com.example.userService.dto.UserRequest;
import com.example.userService.dto.UserResponse;

public interface IUserService {

    UserResponse createUser(UserRequest request);
    UserResponse login(String token);
}
