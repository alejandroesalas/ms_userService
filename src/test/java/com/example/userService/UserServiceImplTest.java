package com.example.userService;

import com.example.userService.dto.PhoneDto;
import com.example.userService.dto.UserRequest;
import com.example.userService.dto.UserResponse;
import com.example.userService.entity.Phone;
import com.example.userService.entity.User;
import com.example.userService.exceptions.UserAlreadyExistException;
import com.example.userService.exceptions.UserNotFoundException;
import com.example.userService.factory.UserServiceDtoFactory;
import com.example.userService.factory.UserServiceFactory;
import com.example.userService.security.JwtUtil;
import com.example.userService.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private com.example.userService.repository.UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceFactory userServiceFactory;
    @Spy
    final UserServiceDtoFactory userServiceDtoFactory = new UserServiceDtoFactory();

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        userServiceFactory = spy(new UserServiceFactory(passwordEncoder));
        userService = new UserServiceImpl(userRepository, jwtUtil, userServiceFactory, userServiceDtoFactory);

    }

    @Test
    void shouldCreateUserSuccessfully() {
        UserRequest request = new UserRequest();
        request.setEmail("test@example.com");
        request.setPassword("a2asfGfdfdf4");
        request.setName("Test User");
        request.setPhones(Set.of(new PhoneDto(null, 12345678L, 1, "57")));

        when(userRepository.findByEmail(eq("test@example.com"))).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(any())).thenReturn("dummy-jwt");

        UserResponse response = userService.createUser(request);
        assertTrue(response.isActive());
        assertNotNull(response.getCreated());
        verify(jwtUtil).generateToken(anyString());
        verify(userServiceFactory).from(any(UserRequest.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowWhenUserAlreadyExists() {
        UserRequest request = new UserRequest();
        request.setEmail("test@example.com");
        request.setPassword("a2asfGfdfdf4");

        when(userRepository.findByEmail(eq("test@example.com"))).thenReturn(Optional.of(new User()));

        RuntimeException exception = assertThrows(UserAlreadyExistException.class, () -> userService.createUser(request));
        assertEquals("User already registered", exception.getMessage());
    }

    @Test
    void shouldLoginSuccessfully() {
        String token = "Bearer valid-token";
        String email = "test@example.com";

        User user = User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .password("encoded")
                .created(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .isActive(true)
                .build();

        when(jwtUtil.getUsernameFromToken(eq("valid-token"))).thenReturn(email);
        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(eq(email))).thenReturn("new-jwt-token");

        UserResponse response = userService.login(token);

        assertEquals(email, response.getEmail());
        assertEquals("new-jwt-token", response.getToken());
        assertNotNull(response.getLastLogin());
    }

    @Test
    void shouldThrowIfUserNotFoundDuringLogin() {
        when(jwtUtil.getUsernameFromToken(any())).thenReturn("no-user@example.com");
        when(userRepository.findByEmail(eq("no-user@example.com"))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.login("Bearer dummy"));
    }
}