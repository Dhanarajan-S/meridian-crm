
package com.meridian.backend.organization.dto;

public record OrganizationResponse(
        Long id,
        String name,
        String slug,
        String status
) {
}
