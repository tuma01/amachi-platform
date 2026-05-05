package com.amachi.app.core.auth.specification;

import com.amachi.app.core.auth.dto.search.UserInvitationSearchDto;
import com.amachi.app.core.auth.entity.UserInvitation;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UserInvitationSpecification implements Specification<UserInvitation> {

    private final UserInvitationSearchDto searchDto;

    @Override
    public Predicate toPredicate(Root<UserInvitation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (searchDto.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), searchDto.getStatus()));
        }

        if (searchDto.getRoleName() != null && !searchDto.getRoleName().isBlank()) {
            predicates.add(cb.equal(root.join("role").get("name"), searchDto.getRoleName()));
        }

        if (searchDto.getEmail() != null && !searchDto.getEmail().isBlank()) {
            predicates.add(cb.like(root.join("user").get("email"), "%" + searchDto.getEmail() + "%"));
        }

        // Aislamiento por tenant gestionado por el filtro Hibernate (@TenantAware en InvitationServiceImpl)

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
