package com.amachi.app.vitalia.medicalcatalog.infrastructure.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.dto.search.MedicalUnitTypeSearchDto;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.entity.MedicalUnitType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MedicalUnitTypeSpecification extends BaseSpecification<MedicalUnitType> {
    private final MedicalUnitTypeSearchDto criteria;

    public MedicalUnitTypeSpecification(MedicalUnitTypeSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<MedicalUnitType> root, @Nullable CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        // ID filter
        if (criteria.getId() != null) {
            predicates.add(cb.equal(root.get("id"), criteria.getId()));
        }

        // Global query filter (code or name)
        if (criteria.getQuery() != null && !criteria.getQuery().isBlank()) {
            String q = "%" + criteria.getQuery().toLowerCase() + "%";
            predicates.add(cb.or(
                cb.like(cb.lower(root.get("code")), q),
                cb.like(cb.lower(root.get("name")), q)
            ));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
