package com.amachi.app.vitalia.medicalcatalog.demographic.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcatalog.demographic.dto.search.CivilStatusSearchDto;
import com.amachi.app.vitalia.medicalcatalog.demographic.entity.CivilStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CivilStatusSpecification extends BaseSpecification<CivilStatus> {
    private final CivilStatusSearchDto criteria;

    public CivilStatusSpecification(CivilStatusSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<CivilStatus> root, @Nullable CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb)); // ✅ Isolation

        // Filtrar por ID exacto
        if (criteria.getId() != null) {
            predicates.add(cb.equal(root.get("id"), criteria.getId()));
        }

        // Filtrar por búsqueda global (Query) en código o nombre
        if (criteria.getQuery() != null && !criteria.getQuery().isBlank()) {
            String q = "%" + criteria.getQuery().toLowerCase() + "%";
            predicates.add(cb.or(
                cb.like(cb.lower(root.get("code")), q),
                cb.like(cb.lower(root.get("name")), q)
            ));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
