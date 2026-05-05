package com.amachi.app.core.geography.country.specification;

import com.amachi.app.core.geography.country.dto.search.CountrySearchDto;
import com.amachi.app.core.geography.country.entity.Country;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CountrySpecification implements Specification<Country> {

    private transient CountrySearchDto criteria;

    @Override
    public Predicate toPredicate(@NonNull Root<Country> root, @Nullable CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {

        if (criteria == null) {
            return cb.conjunction();
        }
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getId() != null) {
            predicates.add(cb.equal(root.get("id"), criteria.getId()));
        }

        if (criteria.getName() != null && !criteria.getName().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + criteria.getName().toLowerCase() + "%"));
        }

        if (criteria.getIso() != null && !criteria.getIso().isBlank()) {
            predicates.add(cb.equal(root.get("iso"), criteria.getIso().trim().toUpperCase()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
