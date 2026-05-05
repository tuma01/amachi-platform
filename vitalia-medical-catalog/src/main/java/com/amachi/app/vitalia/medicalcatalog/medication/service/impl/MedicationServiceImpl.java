package com.amachi.app.vitalia.medicalcatalog.medication.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcatalog.kinship.entity.Kinship;
import com.amachi.app.vitalia.medicalcatalog.kinship.event.KinshipChangedEvent;
import com.amachi.app.vitalia.medicalcatalog.medication.dto.MedicationDto;
import com.amachi.app.vitalia.medicalcatalog.medication.dto.search.MedicationSearchDto;
import com.amachi.app.vitalia.medicalcatalog.medication.entity.Medication;
import com.amachi.app.vitalia.medicalcatalog.medication.event.MedicationChangedEvent;
import com.amachi.app.vitalia.medicalcatalog.medication.mapper.MedicationMapper;
import com.amachi.app.vitalia.medicalcatalog.medication.repository.MedicationRepository;
import com.amachi.app.vitalia.medicalcatalog.medication.service.MedicationService;
import com.amachi.app.vitalia.medicalcatalog.medication.specification.MedicationSpecification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Medication Service Implementation.
 * Patrón validado: DTO-First con hooks de mapeo.
 */
@Service
@RequiredArgsConstructor
@Getter
@Transactional(readOnly = true)
public class MedicationServiceImpl extends BaseService<Medication, MedicationDto, MedicationSearchDto> implements MedicationService {

    private final MedicationRepository repository;
    private final MedicationMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected Specification<Medication> buildSpecification(MedicationSearchDto searchDto) {
        return new MedicationSpecification(searchDto);
    }

    @Override
    protected Medication mapToEntity(MedicationDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(MedicationDto dto, Medication existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected void publishCreatedEvent(Medication entity) {
        publishChanged(entity);
    }

    @Override
    protected void publishUpdatedEvent(Medication entity) {
        publishChanged(entity);
    }

    private void publishChanged(Medication entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(
                    new MedicationChangedEvent(entity.getId(), entity.getCode(), entity.getGenericName())
            );
        }
    }
}
