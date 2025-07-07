package com.example.userService.dto;

import com.example.userService.entity.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class UserRequest {
    @Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    @NotNull
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=(?:.*\\d.*){2})[A-Za-z\\d]{8,12}$")
    @NotNull
    private String password;

    private String name;
    private List<Phone> phones;
}
