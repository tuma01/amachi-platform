package com.amachi.app.vitalia.medicalcore.medicationdispense.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.medicationdispense.dto.search.MedicationDispenseSearchDto;
import com.amachi.app.vitalia.medicalcore.medicationdispense.entity.MedicationDispense;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class MedicationDispenseSpecification extends BaseSpecification<MedicationDispense> {

    private final MedicationDispenseSearchDto criteria;

    public MedicationDispenseSpecification(MedicationDispenseSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<MedicationDispense> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getPatientId() != null)
            predicates.add(cb.equal(root.get("patient").get("id"), criteria.getPatientId()));

        if (criteria.getPrescriptionId() != null)
            predicates.add(cb.equal(root.get("prescription").get("id"), criteria.getPrescriptionId()));

        if (criteria.getDispenserId() != null)
            predicates.add(cb.equal(root.get("dispenser").get("id"), criteria.getDispenserId()));

        if (criteria.getStatus() != null)
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
