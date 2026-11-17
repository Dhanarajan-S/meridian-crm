
package com.meridian.backend.user.dto;

public record UserResponse(
        Long id,
        Long organizationId,
        String firstName,
        String lastName,
        String email,
        String status) {
}
