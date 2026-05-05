package com.amachi.app.vitalia.medicalcore.hospital.service.impl;

import com.amachi.app.core.auth.context.AuthContextHolder;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.exception.ResourceNotFoundException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.hospital.entity.Hospital;
import com.amachi.app.vitalia.medicalcore.hospital.dto.search.HospitalSearchDto;
import com.amachi.app.vitalia.medicalcore.hospital.event.HospitalCreatedEvent;
import com.amachi.app.vitalia.medicalcore.hospital.repository.HospitalRepository;
import com.amachi.app.vitalia.medicalcore.hospital.specification.HospitalSpecification;
import com.amachi.app.vitalia.medicalcore.hospital.service.HospitalService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class HospitalServiceImpl extends BaseService<Hospital, Hospital, HospitalSearchDto> implements HospitalService {

    private final HospitalRepository repository;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected CommonRepository<Hospital, Long> getRepository() {
        return repository;
    }

    @Override
    protected Specification<Hospital> buildSpecification(HospitalSearchDto searchDto) {
        return new HospitalSpecification(searchDto);
    }

    @Override
    protected DomainEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    protected Hospital mapToEntity(Hospital entity) {
        return entity;
    }

    @Override
    protected void mergeEntities(Hospital dto, Hospital existing) {
        // En este servicio, la mezcla se realiza directamente sobre la entidad
    }

    @Override
    protected void publishCreatedEvent(Hospital entity) {
        eventPublisher.publish(new HospitalCreatedEvent(
                entity.getId(),
                entity.getCode(),
                entity.getLegalName(),
                entity.getTaxId()
        ));
    }

    @Override
    protected void publishUpdatedEvent(Hospital entity) {
        // No update event for hospital yet
    }

    @Override
    public Hospital create(Hospital entity) {
        // Validar Tax ID único para evitar hospitales duplicados en la plataforma
        if (entity.getTaxId() != null && repository.existsByTaxId(entity.getTaxId().trim().toUpperCase())) {
            throw new BusinessException("Hospital with Tax ID '" + entity.getTaxId() + "' already exists in the platform");
        }
        return super.create(entity);
    }

    @Override
    public Hospital update(Long id, Hospital entity) {
        Long tenantId = AuthContextHolder.get().getTenantId();
        if (!id.equals(tenantId)) {
            throw new BusinessException("Unauthorized: You can only update your own hospital profile.");
        }
        
        Hospital existing = getById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Hospital", "id", id);
        }

        entity.setId(id);
        return super.update(id, entity);
    }
}
