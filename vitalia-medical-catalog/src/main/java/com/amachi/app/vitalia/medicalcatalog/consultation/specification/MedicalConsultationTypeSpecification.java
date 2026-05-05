package com.amachi.app.vitalia.medicalcatalog.consultation.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcatalog.consultation.dto.search.MedicalConsultationTypeSearchDto;
import com.amachi.app.vitalia.medicalcatalog.consultation.entity.MedicalConsultationType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MedicalConsultationTypeSpecification extends BaseSpecification<MedicalConsultationType> {
    private final MedicalConsultationTypeSearchDto criteria;

    public MedicalConsultationTypeSpecification(MedicalConsultationTypeSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<MedicalConsultationType> root, @Nullable CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        // ID filter
        if (criteria.getId() != null) {
            predicates.add(cb.equal(root.get("id"), criteria.getId()));
        }

        // Global query filter (code or name)
        if (criteria.getQuery() != null && !criteria.getQuery().isBlank()) {
            String q = "%" + criteria.getQuery().toLowerCase() + "%";
            predicates.add(cb.or(
                cb.like(cb.lower(root.get("code")), q),
                cb.like(cb.lower(root.get("name")), q)
            ));
        }

        // Specialty filter
        if (criteria.getSpecialtyId() != null) {
            predicates.add(cb.equal(root.get("specialty").get("id"), criteria.getSpecialtyId()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
