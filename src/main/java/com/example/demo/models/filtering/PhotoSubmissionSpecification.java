package com.example.demo.models.filtering;

import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.filtering.PhotoSubmissionFilterOptions;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class PhotoSubmissionSpecification {

    public static Specification<PhotoSubmission> filterByOptions(PhotoSubmissionFilterOptions options) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (options.getTitle() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("title"), options.getTitle()));
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