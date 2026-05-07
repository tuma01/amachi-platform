package com.amachi.app.vitalia.medicalcore.medicalhistory.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.search.IllnessSearchDto;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

public class IllnessSpecification<T> extends BaseSpecification<T> {

    private final IllnessSearchDto criteria;

    public IllnessSpecification(IllnessSearchDto criteria) { this.criteria = criteria; }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));
        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getMedicalHistoryId() != null)
            predicates.add(cb.equal(root.get("medicalHistory").get("id"), criteria.getMedicalHistoryId()));
        if (criteria.getName() != null && !criteria.getName().isBlank())
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + criteria.getName().toLowerCase() + "%"));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
