
package com.meridian.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserStatusRequest(

    @NotBlank(message = "Status is required")
    @Pattern(
        regexp = "(?i)ACTIVE|INACTIVE",
        message = "Status must be ACTIVE or INACTIVE"
    )
    String status

) {}
