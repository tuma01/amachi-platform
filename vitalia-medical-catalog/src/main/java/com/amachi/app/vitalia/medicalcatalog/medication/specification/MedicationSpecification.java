package com.amachi.app.vitalia.medicalcatalog.medication.specification;

import com.amachi.app.vitalia.medicalcatalog.medication.dto.search.MedicationSearchDto;
import com.amachi.app.vitalia.medicalcatalog.medication.entity.Medication;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MedicationSpecification implements Specification<Medication> {
    private final MedicationSearchDto criteria;

    public MedicationSpecification(MedicationSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<Medication> root, @Nullable CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria != null) {
            if (criteria.getId() != null) {
                predicates.add(cb.equal(root.get("id"), criteria.getId()));
            }
            if (criteria.getCode() != null && !criteria.getCode().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("code")), "%" + criteria.getCode().toLowerCase() + "%"));
            }
            if (criteria.getQuery() != null && !criteria.getQuery().isBlank()) {
                String q = "%" + criteria.getQuery().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("genericName")), q),
                    cb.like(cb.lower(root.get("commercialName")), q)
                ));
            }
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
