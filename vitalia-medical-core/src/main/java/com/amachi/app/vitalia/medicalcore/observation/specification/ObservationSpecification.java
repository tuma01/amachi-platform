package com.amachi.app.vitalia.medicalcore.observation.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.observation.dto.search.ObservationSearchDto;
import com.amachi.app.vitalia.medicalcore.observation.entity.Observation;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class ObservationSpecification extends BaseSpecification<Observation> {

    private final ObservationSearchDto criteria;

    public ObservationSpecification(ObservationSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Observation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getPatientId() != null)
            predicates.add(cb.equal(root.get("patient").get("id"), criteria.getPatientId()));

        if (criteria.getEncounterId() != null)
            predicates.add(cb.equal(root.get("encounter").get("id"), criteria.getEncounterId()));

        if (criteria.getDoctorId() != null)
            predicates.add(cb.equal(root.get("doctor").get("id"), criteria.getDoctorId()));

        if (criteria.getCode() != null && !criteria.getCode().isBlank())
            predicates.add(cb.like(cb.lower(root.get("code")), "%" + criteria.getCode().toLowerCase() + "%"));

        if (criteria.getStatus() != null)
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
