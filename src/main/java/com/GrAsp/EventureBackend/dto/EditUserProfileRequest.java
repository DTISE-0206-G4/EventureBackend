package com.GrAsp.EventureBackend.dto;


import com.GrAsp.EventureBackend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditUserProfileRequest {
    private String name;
    private String description;
    private String profileImage;
}
