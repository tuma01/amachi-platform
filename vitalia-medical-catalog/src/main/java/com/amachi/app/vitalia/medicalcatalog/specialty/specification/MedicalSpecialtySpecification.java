package com.amachi.app.vitalia.medicalcatalog.specialty.specification;

import com.amachi.app.vitalia.medicalcatalog.specialty.dto.search.MedicalSpecialtySearchDto;
import com.amachi.app.vitalia.medicalcatalog.specialty.entity.MedicalSpecialty;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MedicalSpecialtySpecification implements Specification<MedicalSpecialty> {
    private final MedicalSpecialtySearchDto criteria;

    public MedicalSpecialtySpecification(MedicalSpecialtySearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<MedicalSpecialty> root, @Nullable CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria != null) {
            if (criteria.getId() != null) {
                predicates.add(cb.equal(root.get("id"), criteria.getId()));
            }
            if (criteria.getCode() != null && !criteria.getCode().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("code")), "%" + criteria.getCode().toLowerCase() + "%"));
            }
            if (criteria.getQuery() != null && !criteria.getQuery().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + criteria.getQuery().toLowerCase() + "%"));
            }
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
