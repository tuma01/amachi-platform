package com.amachi.app.vitalia.medicalcore.hospitalization.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.search.HospitalizationSearchDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.entity.Hospitalization;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class HospitalizationSpecification extends BaseSpecification<Hospitalization> {

    private final HospitalizationSearchDto criteria;

    public HospitalizationSpecification(HospitalizationSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Hospitalization> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getPatientId() != null)
            predicates.add(cb.equal(root.get("patient").get("id"), criteria.getPatientId()));
        if (criteria.getDoctorId() != null)
            predicates.add(cb.equal(root.get("doctor").get("id"), criteria.getDoctorId()));
        if (criteria.getUnitId() != null)
            predicates.add(cb.equal(root.get("unit").get("id"), criteria.getUnitId()));
        if (criteria.getBedId() != null)
            predicates.add(cb.equal(root.get("bed").get("id"), criteria.getBedId()));
        if (criteria.getStatus() != null)
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
        if (criteria.getAdmissionDateFrom() != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("admissionDate"), criteria.getAdmissionDateFrom()));
        if (criteria.getAdmissionDateTo() != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("admissionDate"), criteria.getAdmissionDateTo()));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
