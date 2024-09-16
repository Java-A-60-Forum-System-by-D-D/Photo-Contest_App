package com.example.demo.models.filtering;

import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.models.filtering.UserFilterOptions;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> filterByOptions(UserFilterOptions options) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (options.getEmail() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("email"), options.getEmail()));
            }
            if (options.getFirstName() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("firstName"), options.getFirstName()));
            }
            if (options.getLastName() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("lastName"), options.getLastName()));
            }
            if (options.getRole() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("role"), options.getRole()));
            }

            if (options.getSortBy() != null && options.getSortDirection() != null) {
                if (options.getSortDirection().equalsIgnoreCase("asc")) {
                    query.orderBy(criteriaBuilder.asc(root.get(options.getSortBy())));
                } else {
                    query.orderBy(criteriaBuilder.desc(root.get(options.getSortBy())));
                }
            }

            return predicate;
        };
    }
}