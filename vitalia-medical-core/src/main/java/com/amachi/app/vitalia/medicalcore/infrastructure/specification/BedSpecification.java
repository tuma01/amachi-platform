package com.amachi.app.vitalia.medicalcore.infrastructure.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.search.BedSearchDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Bed;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BedSpecification extends BaseSpecification<Bed> {

    private transient BedSearchDto criteria;

    @Override
    public Predicate toPredicate(Root<Bed> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getId() != null) {
            predicates.add(cb.equal(root.get("id"), criteria.getId()));
        }

        if (criteria.getRoomId() != null) {
            predicates.add(cb.equal(root.get("room").get("id"), criteria.getRoomId()));
        }

        if (criteria.getBedCode() != null && !criteria.getBedCode().isBlank()) {
            predicates.add(cb.equal(cb.upper(root.get("bedCode")), criteria.getBedCode().toUpperCase()));
        }

        if (criteria.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
        }

        if (criteria.getIsOccupied() != null) {
            predicates.add(cb.equal(root.get("isOccupied"), criteria.getIsOccupied()));
        }

        if (criteria.getActive() != null) {
            predicates.add(cb.equal(root.get("active"), criteria.getActive()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
