
package com.meridian.backend.user.service;

import com.meridian.backend.common.exception.InvalidRequestException;
import com.meridian.backend.common.dto.PageResponse;
import com.meridian.backend.organization.entity.Organization;
import com.meridian.backend.organization.repository.OrganizationRepository;
import com.meridian.backend.user.dto.UserRequest;
import com.meridian.backend.user.dto.UserResponse;
import com.meridian.backend.user.dto.UserStatusRequest;
import com.meridian.backend.user.dto.UserUpdateRequest;
import com.meridian.backend.user.entity.User;
import com.meridian.backend.user.repository.UserRepository;
import com.meridian.backend.user.specification.UserSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            OrganizationRepository organizationRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(UserRequest request) {

        Organization organization = organizationRepository
                .findById(request.organizationId())
                .orElseThrow(() -> new java.util.NoSuchElementException("Organization not found"));

        if (userRepository.existsByOrganizationIdAndEmail(
                request.organizationId(),
                request.email())) {

            throw new IllegalArgumentException(
                    "Email is already registered in this organization");
        }

        String passwordHash = passwordEncoder.encode(request.password());

        User user = new User(
                organization,
                request.firstName(),
                request.lastName(),
                request.email(),
                passwordHash);

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new java.util.NoSuchElementException("User not found"));

        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByOrganization(Long organizationId) {

        if (!organizationRepository.existsById(organizationId)) {
            throw new java.util.NoSuchElementException("Organization not found");
        }

        return userRepository.findByOrganizationId(organizationId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getOrganization().getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getStatus());
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (userRepository.existsByOrganizationIdAndEmail(
                user.getOrganization().getId(),
                request.email())
                && !user.getEmail().equalsIgnoreCase(request.email())) {

            throw new IllegalArgumentException(
                    "Email is already registered in this organization");
        }

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());

        User updatedUser = userRepository.save(user);

        return toResponse(updatedUser);
    }

    public UserResponse updateUserStatus(Long id, UserStatusRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        user.setStatus(request.status().toUpperCase());

        User updatedUser = userRepository.save(user);

        return toResponse(updatedUser);
    }

    public PageResponse<UserResponse> getUsers(int page,
            int size,
            String search,
            String status,
            String sortBy,
            String direction) {
        if (page < 0) {
            throw new InvalidRequestException("Page number cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new InvalidRequestException("Page size must be between 1 and 100");
        }

        List<String> allowedSortFields = List.of(
                "id", "firstName", "lastName", "email", "status");

        if (sortBy == null || !allowedSortFields.contains(sortBy)) {
            throw new InvalidRequestException("Invalid sort field");
        }

        if (direction == null) {
            throw new InvalidRequestException("Direction must be asc or desc");
        }

        Sort.Direction sortDirection;

        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException exception) {
            throw new InvalidRequestException("Direction must be asc or desc");
        }

        if (status != null && !status.isBlank()) {
            String normalizedStatus = status.trim().toUpperCase();

            if (!normalizedStatus.equals("ACTIVE")
                    && !normalizedStatus.equals("INACTIVE")) {
                throw new InvalidRequestException(
                        "Status must be ACTIVE or INACTIVE");
            }

            status = normalizedStatus;
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy));

        Page<UserResponse> userPage = userRepository.findAll(
                UserSpecification.filterUsers(search, status),
                pageable).map(this::toResponse);

        return PageResponse.from(userPage);
    }

}
