package com.amachi.app.vitalia.medicalcore.medicalhistory.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.search.MedicalHistorySearchDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.MedicalHistory;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

public class MedicalHistorySpecification extends BaseSpecification<MedicalHistory> {

    private final MedicalHistorySearchDto criteria;

    public MedicalHistorySpecification(MedicalHistorySearchDto criteria) { this.criteria = criteria; }

    @Override
    public Predicate toPredicate(Root<MedicalHistory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));
        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getPatientId() != null)
            predicates.add(cb.equal(root.get("patient").get("id"), criteria.getPatientId()));
        if (criteria.getHistoryNumber() != null && !criteria.getHistoryNumber().isBlank())
            predicates.add(cb.like(cb.lower(root.get("historyNumber")), "%" + criteria.getHistoryNumber().toLowerCase() + "%"));
        if (criteria.getStatus() != null)
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
        if (criteria.getIsCurrent() != null)
            predicates.add(cb.equal(root.get("isCurrent"), criteria.getIsCurrent()));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
