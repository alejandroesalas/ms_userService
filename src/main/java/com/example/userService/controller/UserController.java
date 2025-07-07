package com.example.userService.controller;

import com.example.userService.dto.UserRequest;
import com.example.userService.dto.UserResponse;
import com.example.userService.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.login(token));
    }
}