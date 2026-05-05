package com.amachi.app.vitalia.medicalcatalog.specialty.service.impl;

import com.amachi.app.core.common.exception.ResourceNotFoundException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcatalog.procedure.entity.MedicalProcedure;
import com.amachi.app.vitalia.medicalcatalog.procedure.event.MedicalProcedureChangedEvent;
import com.amachi.app.vitalia.medicalcatalog.specialty.dto.MedicalSpecialtyDto;
import com.amachi.app.vitalia.medicalcatalog.specialty.dto.search.MedicalSpecialtySearchDto;
import com.amachi.app.vitalia.medicalcatalog.specialty.entity.MedicalSpecialty;
import com.amachi.app.vitalia.medicalcatalog.specialty.event.MedicalSpecialtyChangedEvent;
import com.amachi.app.vitalia.medicalcatalog.specialty.mapper.MedicalSpecialtyMapper;
import com.amachi.app.vitalia.medicalcatalog.specialty.repository.MedicalSpecialtyRepository;
import com.amachi.app.vitalia.medicalcatalog.specialty.service.MedicalSpecialtyService;
import com.amachi.app.vitalia.medicalcatalog.specialty.specification.MedicalSpecialtySpecification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Medical Specialty Service Implementation.
 * Patrón validado: DTO-First con hooks de mapeo.
 */
@Service
@RequiredArgsConstructor
@Getter
@Transactional(readOnly = true)
public class MedicalSpecialtyServiceImpl extends BaseService<MedicalSpecialty, MedicalSpecialtyDto, MedicalSpecialtySearchDto> implements MedicalSpecialtyService {

    private final MedicalSpecialtyRepository repository;
    private final MedicalSpecialtyMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected CommonRepository<MedicalSpecialty, Long> getRepository() {
        return repository;
    }

    @Override
    protected DomainEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    protected Specification<MedicalSpecialty> buildSpecification(MedicalSpecialtySearchDto searchDto) {
        return new MedicalSpecialtySpecification(searchDto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        MedicalSpecialty entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalSpecialty", "not found", id));

        repository.delete(entity);
    }

    @Override
    protected MedicalSpecialty mapToEntity(MedicalSpecialtyDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(MedicalSpecialtyDto dto, MedicalSpecialty existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected void publishCreatedEvent(MedicalSpecialty entity) {
        publishChanged(entity);
    }

    @Override
    protected void publishUpdatedEvent(MedicalSpecialty entity) {
        publishChanged(entity);
    }

    private void publishChanged(MedicalSpecialty entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(
                    new MedicalSpecialtyChangedEvent(entity.getId(), entity.getCode(), entity.getName())
            );
        }
    }
}
