package com.amachi.app.vitalia.medicalcore.infrastructure.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.search.DepartmentUnitSearchDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.DepartmentUnit;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class DepartmentUnitSpecification extends BaseSpecification<DepartmentUnit> {

    private transient DepartmentUnitSearchDto criteria;

    @Override
    public Predicate toPredicate(Root<DepartmentUnit> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getId() != null) {
            predicates.add(cb.equal(root.get("id"), criteria.getId()));
        }

        if (criteria.getName() != null && !criteria.getName().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + criteria.getName().toLowerCase() + "%"));
        }

        if (criteria.getCode() != null && !criteria.getCode().isBlank()) {
            predicates.add(cb.equal(cb.upper(root.get("code")), criteria.getCode().toUpperCase()));
        }

        if (criteria.getFloor() != null && !criteria.getFloor().isBlank()) {
            predicates.add(cb.equal(root.get("floor"), criteria.getFloor()));
        }

        if (criteria.getIsClinical() != null) {
            predicates.add(cb.equal(root.get("isClinical"), criteria.getIsClinical()));
        }

        if (criteria.getActive() != null) {
            predicates.add(cb.equal(root.get("active"), criteria.getActive()));
        }

        if (criteria.getParentUnitId() != null) {
            predicates.add(cb.equal(root.get("parentUnit").get("id"), criteria.getParentUnitId()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
