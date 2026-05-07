package com.amachi.app.vitalia.medicalcore.doctor.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.doctor.dto.search.DoctorHospitalAssignmentSearchDto;
import com.amachi.app.vitalia.medicalcore.doctor.entity.DoctorHospitalAssignment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class DoctorHospitalAssignmentSpecification extends BaseSpecification<DoctorHospitalAssignment> {

    private final DoctorHospitalAssignmentSearchDto criteria;

    public DoctorHospitalAssignmentSpecification(DoctorHospitalAssignmentSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<DoctorHospitalAssignment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getDoctorId() != null)
            predicates.add(cb.equal(root.get("doctor").get("id"), criteria.getDoctorId()));

        if (criteria.getHospitalId() != null)
            predicates.add(cb.equal(root.get("hospital").get("id"), criteria.getHospitalId()));

        if (criteria.getIsPrimary() != null)
            predicates.add(cb.equal(root.get("isPrimary"), criteria.getIsPrimary()));

        // Filtrar vigentes: endDate es null
        if (Boolean.TRUE.equals(criteria.getActive()))
            predicates.add(cb.isNull(root.get("endDate")));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
