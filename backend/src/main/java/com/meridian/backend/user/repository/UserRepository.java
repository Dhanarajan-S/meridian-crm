
package com.meridian.backend.user.repository;

import com.meridian.backend.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByOrganizationId(Long organizationId);

    boolean existsByOrganizationIdAndEmail(
            Long organizationId,
            String email);
}
