package com.amachi.app.vitalia.medicalcore.nurse.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.nurse.dto.search.NurseSearchDto;
import com.amachi.app.vitalia.medicalcore.nurse.entity.Nurse;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class NurseSpecification extends BaseSpecification<Nurse> {

    private final NurseSearchDto criteria;

    public NurseSpecification(NurseSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Nurse> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getLicenseNumber() != null && !criteria.getLicenseNumber().isBlank())
            predicates.add(cb.like(cb.lower(root.get("licenseNumber")), "%" + criteria.getLicenseNumber().toLowerCase() + "%"));

        if (criteria.getDepartmentUnitId() != null)
            predicates.add(cb.equal(root.get("departmentUnit").get("id"), criteria.getDepartmentUnitId()));

        if (criteria.getWorkShift() != null)
            predicates.add(cb.equal(root.get("workShift"), criteria.getWorkShift()));

        if (criteria.getRank() != null && !criteria.getRank().isBlank())
            predicates.add(cb.like(cb.lower(root.get("rank")), "%" + criteria.getRank().toLowerCase() + "%"));

        if (criteria.getName() != null && !criteria.getName().isBlank()) {
            String pattern = "%" + criteria.getName().toLowerCase() + "%";
            predicates.add(cb.or(
                cb.like(cb.lower(root.join("person").get("firstName")), pattern),
                cb.like(cb.lower(root.join("person").get("lastName")), pattern)
            ));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
