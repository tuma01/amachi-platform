package com.amachi.app.core.domain.hospital.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.core.domain.hospital.dto.search.HospitalSearchDto;
import com.amachi.app.core.domain.hospital.entity.Hospital;
import com.amachi.app.core.domain.hospital.event.HospitalCreatedEvent;
import com.amachi.app.core.domain.hospital.repository.HospitalRepository;
import com.amachi.app.core.domain.hospital.specification.HospitalSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl extends BaseService<Hospital, HospitalSearchDto> {

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
        entity.setId(id);
        return super.update(id, entity);
    }
}
