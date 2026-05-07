package com.amachi.app.vitalia.medicalcore.habit.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.habit.dto.search.HabitSearchDto;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

public class HabitSpecification<T> extends BaseSpecification<T> {

    private final HabitSearchDto criteria;

    public HabitSpecification(HabitSearchDto criteria) { this.criteria = criteria; }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));
        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getMedicalHistoryId() != null)
            predicates.add(cb.equal(root.get("medicalHistory").get("id"), criteria.getMedicalHistoryId()));
        if (criteria.getIsCurrent() != null)
            predicates.add(cb.equal(root.get("isCurrent"), criteria.getIsCurrent()));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
