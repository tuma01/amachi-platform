package com.amachi.app.vitalia.medicalcore.profile.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.profile.dto.search.UserProfileSearchDto;
import com.amachi.app.vitalia.medicalcore.profile.entity.UserProfile;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class UserProfileSpecification extends BaseSpecification<UserProfile> {

    private final UserProfileSearchDto criteria;

    public UserProfileSpecification(UserProfileSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<UserProfile> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria != null && criteria.getBiography() != null && !criteria.getBiography().isBlank())
            predicates.add(cb.like(cb.lower(root.get("biography")), "%" + criteria.getBiography().toLowerCase() + "%"));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
