package com.amachi.app.vitalia.medicalcatalog.allergy.specification;

import com.amachi.app.vitalia.medicalcatalog.allergy.dto.search.AllergySearchDto;
import com.amachi.app.vitalia.medicalcatalog.allergy.entity.Allergy;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AllergySpecification implements Specification<Allergy> {
    private final AllergySearchDto criteria;

    public AllergySpecification(AllergySearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<Allergy> root, @Nullable CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria != null) {
            if (criteria.getId() != null) {
                predicates.add(cb.equal(root.get("id"), criteria.getId()));
            }
            if (criteria.getCode() != null && !criteria.getCode().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("code")), "%" + criteria.getCode().toLowerCase() + "%"));
            }
            if (criteria.getQuery() != null && !criteria.getQuery().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + criteria.getQuery().toLowerCase() + "%"));
            }
            if (criteria.getType() != null && !criteria.getType().isBlank()) {
                predicates.add(cb.equal(root.get("type"), criteria.getType()));
            }
            if (criteria.getCriticality() != null && !criteria.getCriticality().isBlank()) {
                predicates.add(cb.equal(root.get("criticality"), criteria.getCriticality()));
            }
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
