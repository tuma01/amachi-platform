package com.amachi.app.vitalia.medicalcore.professional.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.professional.dto.search.ProfessionalInfoSearchDto;
import com.amachi.app.vitalia.medicalcore.professional.entity.ProfessionalInfo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class ProfessionalInfoSpecification extends BaseSpecification<ProfessionalInfo> {

    private final ProfessionalInfoSearchDto criteria;

    public ProfessionalInfoSpecification(ProfessionalInfoSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<ProfessionalInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getPersonId() != null)
            predicates.add(cb.equal(root.get("person").get("id"), criteria.getPersonId()));

        if (criteria.getRoleContext() != null)
            predicates.add(cb.equal(root.get("roleContext"), criteria.getRoleContext()));

        if (criteria.getIsCurrent() != null)
            predicates.add(cb.equal(root.get("isCurrent"), criteria.getIsCurrent()));

        if (criteria.getOrganizationId() != null)
            predicates.add(cb.equal(root.get("organizationId"), criteria.getOrganizationId()));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
