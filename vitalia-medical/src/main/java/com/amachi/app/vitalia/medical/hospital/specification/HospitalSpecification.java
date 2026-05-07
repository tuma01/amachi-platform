package com.amachi.app.core.domain.hospital.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.core.domain.hospital.dto.search.HospitalSearchDto;
import com.amachi.app.core.domain.hospital.entity.Hospital;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class HospitalSpecification extends BaseSpecification<Hospital> {

    private final HospitalSearchDto criteria;

    @Override
    public Predicate toPredicate(Root<Hospital> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb)); // ✅ Isolation

        if (criteria.getId() != null) {
            predicates.add(cb.equal(root.get("id"), criteria.getId()));
        }

        if (criteria.getLegalName() != null && !criteria.getLegalName().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("legalName")), 
                    "%" + criteria.getLegalName().toLowerCase() + "%"));
        }

        if (criteria.getTaxId() != null && !criteria.getTaxId().isBlank()) {
            predicates.add(cb.equal(cb.lower(root.get("taxId")), 
                    criteria.getTaxId().toLowerCase()));
        }

        if (criteria.getMedicalLicense() != null && !criteria.getMedicalLicense().isBlank()) {
            predicates.add(cb.equal(cb.lower(root.get("medicalLicense")), 
                    criteria.getMedicalLicense().toLowerCase()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
