package com.example.demo.models.filtering;

import com.example.demo.models.Contest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ContestSpecification {
    public static Specification<Contest> filterByOptions(ContestFilterOptions options) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (options.getTitle() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("title"), options.getTitle()));
            }
            if (options.getCategory() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("category"), options.getCategory().getName()));
            }
            if (options.getPhase() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("phase"), options.getPhase().toString()));
            }



            return predicate;
        };
    }
}
