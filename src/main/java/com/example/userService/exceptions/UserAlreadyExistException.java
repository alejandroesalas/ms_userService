package com.example.userService.exceptions;

public class UserAlreadyExistException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "User already registered";
    public UserAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }
}
