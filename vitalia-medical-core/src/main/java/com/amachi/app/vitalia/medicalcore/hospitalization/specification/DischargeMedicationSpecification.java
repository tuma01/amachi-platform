package com.amachi.app.vitalia.medicalcore.hospitalization.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.search.DischargeMedicationSearchDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.entity.DischargeMedication;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class DischargeMedicationSpecification extends BaseSpecification<DischargeMedication> {

    private final DischargeMedicationSearchDto criteria;

    public DischargeMedicationSpecification(DischargeMedicationSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<DischargeMedication> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getHospitalizationId() != null)
            predicates.add(cb.equal(root.get("hospitalization").get("id"), criteria.getHospitalizationId()));
        if (criteria.getMedicationId() != null)
            predicates.add(cb.equal(root.get("medication").get("id"), criteria.getMedicationId()));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
