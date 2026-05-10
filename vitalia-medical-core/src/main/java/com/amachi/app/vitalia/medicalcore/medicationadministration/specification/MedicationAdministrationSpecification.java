package com.amachi.app.vitalia.medicalcore.medicationadministration.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.medicationadministration.dto.search.MedicationAdministrationSearchDto;
import com.amachi.app.vitalia.medicalcore.medicationadministration.entity.MedicationAdministration;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class MedicationAdministrationSpecification extends BaseSpecification<MedicationAdministration> {

    private final MedicationAdministrationSearchDto criteria;

    public MedicationAdministrationSpecification(MedicationAdministrationSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<MedicationAdministration> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getPatientId() != null)
            predicates.add(cb.equal(root.get("patient").get("id"), criteria.getPatientId()));

        if (criteria.getPrescriptionId() != null)
            predicates.add(cb.equal(root.get("prescription").get("id"), criteria.getPrescriptionId()));

        if (criteria.getNurseId() != null)
            predicates.add(cb.equal(root.get("nurse").get("id"), criteria.getNurseId()));

        if (criteria.getStatus() != null)
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
