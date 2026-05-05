package com.amachi.app.core.common.service;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.core.common.entity.BaseEntity;
import com.amachi.app.core.common.entity.SoftDeletable;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.ResourceNotFoundException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.repository.TenantCommonRepository;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Servicio base enterprise para toda la aplicación.
 * SaaS Elite Tier: Refactorizado para contrato DTO/Command y seguridad Staff Level.
 * ESTADO: CONGELADO - NO MODIFICAR.
 */
public abstract class BaseService<E extends BaseEntity, D, F extends BaseSearchDto>
        implements GenericService<E, D, F> {

    protected abstract CommonRepository<E, Long> getRepository();
    protected abstract Specification<E> buildSpecification(F searchDto);
    protected abstract DomainEventPublisher getEventPublisher();

    // ✔ Mejora segura para proxies de Spring
//    private final boolean tenantAware =
//            AnnotationUtils.findAnnotation(getClass(), TenantAware.class) != null;

    private final boolean tenantAware =
            AnnotationUtils.findAnnotation(
                    ClassUtils.getUserClass(this),
                    TenantAware.class
            ) != null;

    @Override
    public List<E> getAll() {
        if (tenantAware) {
            Long tenantId = TenantContext.getTenantId();
            if (tenantId == null) {
                throw new IllegalStateException("Critical Security Breach: TenantContext not initialized in a @TenantAware service");
            }
            return getRepository().findAll(
                    (root, query, cb) ->
                            cb.equal(root.get("tenantId"), tenantId)
            );
        }
        return getRepository().findAll();
    }

    @Override
    public Page<E> getAll(F searchDto, Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Specification<E> spec = (searchDto != null)
                ? buildSpecification(searchDto)
                : null;
        if (tenantAware) {
            Long tenantId = TenantContext.getTenantId();
            if (tenantId == null) {
                throw new IllegalStateException("Critical Security Breach: TenantContext not initialized in a @TenantAware service");
            }
            Specification<E> tenantSpec = (root, query, cb) ->
                    cb.equal(root.get("tenantId"), tenantId);
            spec = (spec == null) ? tenantSpec : spec.and(tenantSpec);
        }
        return getRepository().findAll(spec, pageable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E getById(Long id) {
        requireNonNull(id, "ID must not be null");

        if (tenantAware && getRepository() instanceof TenantCommonRepository) {
            Long tenantId = TenantContext.getTenantId();
            if (tenantId == null) {
                throw new IllegalStateException("Critical Security Breach: TenantContext not initialized in a @TenantAware service");
            }
            return ((TenantCommonRepository<E, Long>) getRepository())
                    .findByIdAndTenantId(id, tenantId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Entity", "err.not_found", id));
        }
        return getRepository()
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Entity", "err.not_found", id));
    }

    protected abstract void publishCreatedEvent(E entity);
    protected abstract void publishUpdatedEvent(E entity);
    protected void publishDeletedEvent(E entity) {}

    protected abstract E mapToEntity(D dto);

    @Override
    @Transactional
    public E create(D dto) {
        requireNonNull(dto, "DTO must not be null");

        E entity = mapToEntity(dto);
        E savedEntity = getRepository().save(entity);

        publishCreatedEvent(savedEntity);
        return savedEntity;
    }

    @Override
    @Transactional
    public E update(Long id, D dto) {
        requireNonNull(id, "ID must not be null");
        requireNonNull(dto, "DTO must not be null");

        E existingEntity = getById(id);
        mergeEntities(dto, existingEntity);

        E savedEntity = getRepository().save(existingEntity);
        publishUpdatedEvent(savedEntity);
        return savedEntity;
    }

    protected abstract void mergeEntities(D dto, E existing);

    @Override
    @Transactional
    public void delete(Long id) {
        E entity = getById(id);

        if (entity instanceof SoftDeletable soft) {
            if (soft.getDeleted()) {
                publishDeletedEvent(entity);
                return;
            }
            soft.delete();
            getRepository().save(entity);
        } else {
            getRepository().delete(entity);
        }
        publishDeletedEvent(entity);
    }
}