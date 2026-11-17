package com.meridian.backend.user.specification;

import com.meridian.backend.user.entity.User;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    private UserSpecification() {
        // Utility class
    }

    public static Specification<User> filterUsers(
            String search,
            String status) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isBlank()) {

                String searchPattern = "%" + search.trim().toLowerCase() + "%";

                Predicate firstName = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("firstName")),
                        searchPattern);

                Predicate lastName = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("lastName")),
                        searchPattern);

                Predicate email = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        searchPattern);

                predicates.add(
                        criteriaBuilder.or(firstName, lastName, email));
            }

            if (status != null && !status.isBlank()) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("status"),
                                status.trim().toUpperCase()));
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0]));
        };
    }
}