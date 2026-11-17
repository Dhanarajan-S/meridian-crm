package com.meridian.backend.organization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record OrganizationStatusRequest(

        @NotBlank(message = "Organization status is required")
        @Pattern(
                regexp = "^(ACTIVE|INACTIVE)$",
                flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "Status must be ACTIVE or INACTIVE"
        )
        String status

) {}