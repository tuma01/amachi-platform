package com.amachi.app.core.common.specification;

import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.entity.SoftDeletable;
import com.amachi.app.core.common.entity.TenantScoped;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Especificación base para filtrado automático de multi-tenencia y borrado lógico.
 * SaaS Elite Tier: Validada para distinguir entre entidades Globales e Inquilino.
 */
public abstract class BaseSpecification<T> implements Specification<T> {

    protected List<Predicate> buildBasePredicates(Root<T> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // 🛡️ Filtro multi-tenant automático (Solo si la entidad es TenantScoped)
        if (TenantScoped.class.isAssignableFrom(root.getJavaType())) {
            Long tenantId = TenantContext.getTenantId();
            if (tenantId != null) {
                predicates.add(cb.equal(root.get("tenantId"), tenantId));
            }
        }

        // 🛡️ Filtro soft delete automático (Solo si la entidad es SoftDeletable)
        if (SoftDeletable.class.isAssignableFrom(root.getJavaType())) {
            predicates.add(cb.equal(root.get("isDeleted"), false));
        }

        return predicates;
    }
}
