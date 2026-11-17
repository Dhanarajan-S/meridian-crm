package com.meridian.backend.organization.specification;

import com.meridian.backend.organization.entity.Organization;
import org.springframework.data.jpa.domain.Specification;

public class OrganizationSpecification {

    private OrganizationSpecification() {
        // Utility class
    }

    public static Specification<Organization> hasName(String search) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + search.toLowerCase() + "%");
    }

    public static Specification<Organization> hasStatus(String status) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                criteriaBuilder.upper(root.get("status")),
                status.toUpperCase());
    }
}