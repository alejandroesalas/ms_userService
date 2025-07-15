package com.example.userService;

import com.example.userService.controller.UserController;
import com.example.userService.exceptions.UserAlreadyExistException;
import com.example.userService.exceptions.UserNotFoundException;
import com.example.userService.security.JwtUtil;
import com.example.userService.service.IUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void shouldReturnBadRequestWhenUserAlreadyExists() throws Exception {
        String body = Files.readString(Paths.get("src/test/resources/request.json"));


        Mockito.when(userService.createUser(Mockito.any())).thenThrow(new UserAlreadyExistException());

        mockMvc.perform(post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].codigo").value(400))
                .andExpect(jsonPath("$.error[0].detail").value("User already registered"));
    }
    @Test
    void shouldReturnBadRequestWhenBadData() throws Exception {
        String body = Files.readString(Paths.get("src/test/resources/invalidRequest.json"));


        Mockito.when(userService.createUser(Mockito.any())).thenThrow(new UserAlreadyExistException());

        mockMvc.perform(post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].codigo").value(400))
                .andExpect(jsonPath("$.error[0].detail").value("email: must be a well-formed email address"));
    }

    @Test
    void shouldReturnInternalError() throws Exception {
        String body = Files.readString(Paths.get("src/test/resources/request.json"));


        Mockito.when(userService.createUser(Mockito.any())).thenThrow(new RuntimeException("something went wrong"));

        mockMvc.perform(post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error[0].codigo").value(500))
                .andExpect(jsonPath("$.error[0].detail").value("Internal error: something went wrong"));
    }

    @Test
    void shouldReturnNotFoundErrorWhenLogin() throws Exception {
        String body = Files.readString(Paths.get("src/test/resources/request.json"));


        Mockito.when(userService.login(Mockito.any())).thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer token"))
                .andExpect(status().isNotFound());
    }

}