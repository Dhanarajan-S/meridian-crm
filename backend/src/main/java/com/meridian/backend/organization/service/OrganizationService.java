
package com.meridian.backend.organization.service;

import com.meridian.backend.organization.entity.Organization;
import com.meridian.backend.organization.repository.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.meridian.backend.organization.specification.OrganizationSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(
            OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public Organization createOrganization(String name, String slug) {

        if (organizationRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException(
                    "An organization with this slug already exists: " + slug);
        }

        Organization organization = new Organization(name, slug);
        return organizationRepository.save(organization);
    }

    // Your @Transactional annotation on the service class defines a transaction
    // boundary for its public methods. If the operation fails, the transaction can
    // be rolled back according to the exception type and transaction configuration.
    @Transactional(readOnly = true)
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Organization not found with ID: " + id));
    }

    public Organization updateOrganization(
            Long id, String name, String slug) {

        Organization organization = getOrganizationById(id);

        if (organizationRepository.existsBySlugAndIdNot(slug, id)) {
            throw new IllegalArgumentException(
                    "An organization with this slug already exists: " + slug);
        }

        organization.setName(name);
        organization.setSlug(slug);

        return organizationRepository.save(organization);
    }

    public void deleteOrganization(Long id) {
        Organization organization = getOrganizationById(id);
        organizationRepository.delete(organization);
    }

    @Transactional(readOnly = true)
    public Page<Organization> getAllOrganizations(Pageable pageable) {
        return organizationRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Organization> searchOrganizations(
            String search,
            String status,
            Pageable pageable) {

        Specification<Organization> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (search != null && !search.isBlank()) {
            specification = specification.and(
                    OrganizationSpecification.hasName(search.trim()));
        }

        if (status != null && !status.isBlank()) {
            specification = specification.and(
                    OrganizationSpecification.hasStatus(status.trim()));
        }

        return organizationRepository.findAll(specification, pageable);
    }

    @Transactional
    public Organization updateOrganizationStatus(Long id, String status) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Organization not found with ID: " + id));

        organization.setStatus(status.toUpperCase());

        return organizationRepository.save(organization);
    }
}
