package com.amachi.app.core.auth.specification;

import com.amachi.app.core.auth.dto.search.UserTenantRoleSearchDto;
import com.amachi.app.core.auth.entity.UserTenantRole;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class UserTenantRoleSpecification implements Specification<UserTenantRole> {

    private transient UserTenantRoleSearchDto criteria;

    @Override
    public Predicate toPredicate(Root<UserTenantRole> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // Filtro por ID del país
        if (criteria.getId() != null) {
            predicates.add(cb.equal(root.get("id"), criteria.getId()));
        }

        // User email
        if (criteria.getUser() != null &&
                criteria.getUser().getEmail() != null &&
                !criteria.getUser().getEmail().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("user").get("email")), "%" + criteria.getUser().getEmail().toLowerCase() + "%"));
        }

        if (criteria.getAssignedAt() != null) {
            predicates.add(cb.equal(root.get("assignedAt"), criteria.getAssignedAt()));
        }

        if (criteria.getRevokedAt() != null) {
            predicates.add(cb.equal(root.get("revokedAt"), criteria.getRevokedAt()));
        }

        if (criteria.getActive() != null) {
            predicates.add(cb.equal(root.get("active"), criteria.getActive()));
        }

        // Retornar todas las condiciones combinadas
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
