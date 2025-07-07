package com.example.userService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class ErrorResponse {
    private List<ErrorDetail> error;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ErrorDetail {
        private Instant timestamp;
        private int codigo;
        private String detail;
    }
}
