package com.amachi.app.vitalia.medicalcore.consultation.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.consultation.dto.search.ConsultationSearchDto;
import com.amachi.app.vitalia.medicalcore.consultation.entity.Consultation;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class ConsultationSpecification extends BaseSpecification<Consultation> {

    private final ConsultationSearchDto criteria;

    public ConsultationSpecification(ConsultationSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Consultation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getPatientId() != null)
            predicates.add(cb.equal(root.get("patient").get("id"), criteria.getPatientId()));
        if (criteria.getDoctorId() != null)
            predicates.add(cb.equal(root.get("doctor").get("id"), criteria.getDoctorId()));
        if (criteria.getMedicalHistoryId() != null)
            predicates.add(cb.equal(root.get("medicalHistory").get("id"), criteria.getMedicalHistoryId()));
        if (criteria.getStatus() != null)
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
        if (criteria.getDateFrom() != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("consultationDate"), criteria.getDateFrom()));
        if (criteria.getDateTo() != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("consultationDate"), criteria.getDateTo()));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
