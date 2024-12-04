package com.GrAsp.EventureBackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LogoutRequest {
    @NotNull
    private String refreshToken;
    @NotNull
    private String accessToken;
}