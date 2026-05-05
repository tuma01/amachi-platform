package com.amachi.app.vitalia.medicalcatalog.procedure.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcatalog.medication.entity.Medication;
import com.amachi.app.vitalia.medicalcatalog.medication.event.MedicationChangedEvent;
import com.amachi.app.vitalia.medicalcatalog.procedure.dto.MedicalProcedureDto;
import com.amachi.app.vitalia.medicalcatalog.procedure.dto.search.MedicalProcedureSearchDto;
import com.amachi.app.vitalia.medicalcatalog.procedure.entity.MedicalProcedure;
import com.amachi.app.vitalia.medicalcatalog.procedure.event.MedicalProcedureChangedEvent;
import com.amachi.app.vitalia.medicalcatalog.procedure.mapper.MedicalProcedureMapper;
import com.amachi.app.vitalia.medicalcatalog.procedure.repository.MedicalProcedureRepository;
import com.amachi.app.vitalia.medicalcatalog.procedure.service.MedicalProcedureService;
import com.amachi.app.vitalia.medicalcatalog.procedure.specification.MedicalProcedureSpecification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Medical Procedure Service Implementation.
 * Patrón validado: DTO-First con hooks de mapeo.
 */
@Service
@RequiredArgsConstructor
@Getter
@Transactional(readOnly = true)
public class MedicalProcedureServiceImpl extends BaseService<MedicalProcedure, MedicalProcedureDto, MedicalProcedureSearchDto> implements MedicalProcedureService {

    private final MedicalProcedureRepository repository;
    private final MedicalProcedureMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected Specification<MedicalProcedure> buildSpecification(MedicalProcedureSearchDto searchDto) {
        return new MedicalProcedureSpecification(searchDto);
    }

    @Override
    protected MedicalProcedure mapToEntity(MedicalProcedureDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(MedicalProcedureDto dto, MedicalProcedure existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected void publishCreatedEvent(MedicalProcedure entity) {
        publishChanged(entity);
    }

    @Override
    protected void publishUpdatedEvent(MedicalProcedure entity) {
        publishChanged(entity);
    }

    private void publishChanged(MedicalProcedure entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(
                    new MedicalProcedureChangedEvent(entity.getId(), entity.getCode(), entity.getName())
            );
        }
    }
}
