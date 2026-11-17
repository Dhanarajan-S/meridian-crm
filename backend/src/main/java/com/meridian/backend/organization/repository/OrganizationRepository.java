package com.meridian.backend.organization.repository;

import com.meridian.backend.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrganizationRepository
        extends JpaRepository<Organization, Long>,
        JpaSpecificationExecutor<Organization> {

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);
}