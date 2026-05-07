package com.amachi.app.vitalia.medicalcore.prescription.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.prescription.dto.search.PrescriptionSearchDto;
import com.amachi.app.vitalia.medicalcore.prescription.entity.Prescription;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionSpecification extends BaseSpecification<Prescription> {

    private final PrescriptionSearchDto criteria;

    public PrescriptionSpecification(PrescriptionSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Prescription> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getPatientId() != null)
            predicates.add(cb.equal(root.get("patient").get("id"), criteria.getPatientId()));

        if (criteria.getDoctorId() != null)
            predicates.add(cb.equal(root.get("doctor").get("id"), criteria.getDoctorId()));

        if (criteria.getEncounterId() != null)
            predicates.add(cb.equal(root.get("encounter").get("id"), criteria.getEncounterId()));

        if (criteria.getStatus() != null)
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
