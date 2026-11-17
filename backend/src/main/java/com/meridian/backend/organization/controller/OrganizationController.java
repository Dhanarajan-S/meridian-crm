
package com.meridian.backend.organization.controller;

//import java.util.List;  
import com.meridian.backend.organization.dto.OrganizationRequest;
import com.meridian.backend.organization.dto.OrganizationResponse;
import com.meridian.backend.organization.entity.Organization;
import com.meridian.backend.organization.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.meridian.backend.common.dto.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.meridian.backend.common.exception.InvalidRequestException;
import com.meridian.backend.organization.dto.OrganizationStatusRequest;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

        private final OrganizationService organizationService;

        public OrganizationController(
                        OrganizationService organizationService) {
                this.organizationService = organizationService;
        }

        @PostMapping
        public ResponseEntity<OrganizationResponse> createOrganization(
                        @Valid @RequestBody OrganizationRequest request) {

                Organization organization = organizationService.createOrganization(
                                request.name(),
                                request.slug());

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(toResponse(organization));
        }

        @GetMapping
        public ResponseEntity<PageResponse<OrganizationResponse>> getAllOrganizations(

                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "id") String sortBy,
                        @RequestParam(defaultValue = "asc") String direction,
                        @RequestParam(required = false) String search,
                        @RequestParam(required = false) String status) {

                if (page < 0) {
                        throw new InvalidRequestException(
                                        "Page number cannot be negative");
                }

                if (size < 1 || size > 100) {
                        throw new InvalidRequestException(
                                        "Page size must be between 1 and 100");
                }

                if (!sortBy.equals("id")
                                && !sortBy.equals("name")
                                && !sortBy.equals("slug")
                                && !sortBy.equals("status")) {
                        throw new InvalidRequestException(
                                        "Invalid sort field: " + sortBy);
                }

                if (!direction.equalsIgnoreCase("asc")
                                && !direction.equalsIgnoreCase("desc")) {
                        throw new InvalidRequestException(
                                        "Direction must be asc or desc");
                }

                if (status != null && !status.isBlank()
                                && !status.equalsIgnoreCase("ACTIVE")
                                && !status.equalsIgnoreCase("INACTIVE")) {
                        throw new InvalidRequestException(
                                        "Status must be ACTIVE or INACTIVE");
                }

                Sort sort = direction.equalsIgnoreCase("desc")
                                ? Sort.by(sortBy).descending()
                                : Sort.by(sortBy).ascending();

                Pageable pageable = PageRequest.of(page, size, sort);

                PageResponse<OrganizationResponse> organizations = PageResponse.from(
                                organizationService.searchOrganizations(search, status, pageable)
                                                .map(this::toResponse));

                return ResponseEntity.ok(organizations);
        }

        @GetMapping("/{id}")
        public ResponseEntity<OrganizationResponse> getOrganizationById(
                        @PathVariable Long id) {

                Organization organization = organizationService.getOrganizationById(id);

                return ResponseEntity.ok(toResponse(organization));
        }

        @PutMapping("/{id}")
        public ResponseEntity<OrganizationResponse> updateOrganization(
                        @PathVariable Long id,
                        @Valid @RequestBody OrganizationRequest request) {

                Organization organization = organizationService.updateOrganization(
                                id,
                                request.name(),
                                request.slug());

                return ResponseEntity.ok(toResponse(organization));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteOrganization(
                        @PathVariable Long id) {

                organizationService.deleteOrganization(id);

                return ResponseEntity.noContent().build();
        }

        private OrganizationResponse toResponse(
                        Organization organization) {

                return new OrganizationResponse(
                                organization.getId(),
                                organization.getName(),
                                organization.getSlug(),
                                organization.getStatus());
        }

        @PatchMapping("/{id}/status")
        public ResponseEntity<OrganizationResponse> updateOrganizationStatus(
                        @PathVariable Long id,
                        @Valid @RequestBody OrganizationStatusRequest request) {

                Organization organization = organizationService.updateOrganizationStatus(
                                id,
                                request.status());

                return ResponseEntity.ok(toResponse(organization));
        }
}
