package com.GrAsp.EventureBackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotNull
    private String oldPassword;

    @NotNull
    private String newPassword;
}
