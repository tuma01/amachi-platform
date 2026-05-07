package com.amachi.app.vitalia.medicalcore.organization.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.organization.dto.search.OrganizationSearchDto;
import com.amachi.app.vitalia.medicalcore.organization.entity.Organization;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class OrganizationSpecification extends BaseSpecification<Organization> {

    private final OrganizationSearchDto criteria;

    public OrganizationSpecification(OrganizationSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Organization> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getName() != null && !criteria.getName().isBlank())
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + criteria.getName().toLowerCase() + "%"));
        if (criteria.getType() != null && !criteria.getType().isBlank())
            predicates.add(cb.equal(root.get("type"), criteria.getType().toUpperCase()));
        if (criteria.getActive() != null)
            predicates.add(cb.equal(root.get("active"), criteria.getActive()));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
