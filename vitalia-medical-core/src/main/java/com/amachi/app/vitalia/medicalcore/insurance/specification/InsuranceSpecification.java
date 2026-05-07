package com.amachi.app.vitalia.medicalcore.insurance.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.insurance.dto.search.InsuranceSearchDto;
import com.amachi.app.vitalia.medicalcore.insurance.entity.Insurance;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

public class InsuranceSpecification extends BaseSpecification<Insurance> {

    private final InsuranceSearchDto criteria;

    public InsuranceSpecification(InsuranceSearchDto criteria) { this.criteria = criteria; }

    @Override
    public Predicate toPredicate(Root<Insurance> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));
        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getMedicalHistoryId() != null)
            predicates.add(cb.equal(root.get("medicalHistory").get("id"), criteria.getMedicalHistoryId()));
        if (criteria.getProviderId() != null)
            predicates.add(cb.equal(root.get("provider").get("id"), criteria.getProviderId()));
        if (criteria.getIsCurrent() != null)
            predicates.add(cb.equal(root.get("isCurrent"), criteria.getIsCurrent()));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
