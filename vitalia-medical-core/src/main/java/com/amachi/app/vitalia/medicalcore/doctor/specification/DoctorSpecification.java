package com.amachi.app.vitalia.medicalcore.doctor.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.doctor.dto.search.DoctorSearchDto;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Especificación de búsqueda para Doctor (SaaS Elite Tier).
 * Garantiza aislamiento de inquilino y filtrado de borrados lógicos.
 */
public class DoctorSpecification extends BaseSpecification<Doctor> {

    private final DoctorSearchDto criteria;

    public DoctorSpecification(DoctorSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Doctor> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // Base (tenant + soft delete)
        predicates.addAll(buildBasePredicates(root, cb));

        if (criteria == null) {
            return cb.and(predicates.toArray(new Predicate[0]));
        }

        // 🔍 licenseNumber
        if (criteria.getLicenseNumber() != null && !criteria.getLicenseNumber().isBlank()) {
            predicates.add(cb.like(
                    cb.lower(root.get("licenseNumber")),
                    "%" + criteria.getLicenseNumber().toLowerCase() + "%"
            ));
        }

        // 🔍 nationalId (via Person)
        if (criteria.getNationalId() != null && !criteria.getNationalId().isBlank()) {
            predicates.add(cb.equal(
                    root.join("person").get("nationalId"),
                    criteria.getNationalId()
            ));
        }

        // 🔍 disponibilidad (mapped to active)
        if (criteria.getIsAvailable() != null) {
            predicates.add(cb.equal(
                    root.get("active"),
                    criteria.getIsAvailable()
            ));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
