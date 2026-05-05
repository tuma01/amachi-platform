package com.amachi.app.core.management.theme.specification;

import com.amachi.app.core.domain.theme.dto.search.ThemeSearchDto;
import com.amachi.app.core.domain.theme.entity.Theme;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ThemeSpecification implements Specification<Theme> {

    private final ThemeSearchDto searchDto;

    @Override
    public Predicate toPredicate(@NonNull Root<Theme> root, @NonNull CriteriaQuery<?> query,
            @NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (searchDto.getId() != null) {
            predicates.add(cb.equal(root.get("id"), searchDto.getId()));
        }

        if (searchDto.getCode() != null && !searchDto.getCode().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("code")), "%" + searchDto.getCode().toLowerCase() + "%"));
        }

        if (searchDto.getName() != null && !searchDto.getName().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + searchDto.getName().toLowerCase() + "%"));
        }

        // Only templates should be visible/manageable in the general themes list
        predicates.add(cb.isTrue(root.get("template")));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
