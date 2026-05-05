package com.amachi.app.vitalia.medicalcatalog.consultation.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcatalog.consultation.dto.MedicalConsultationTypeDto;
import com.amachi.app.vitalia.medicalcatalog.consultation.dto.search.MedicalConsultationTypeSearchDto;
import com.amachi.app.vitalia.medicalcatalog.consultation.entity.MedicalConsultationType;
import com.amachi.app.vitalia.medicalcatalog.consultation.event.MedicalConsultationTypeChangedEvent;
import com.amachi.app.vitalia.medicalcatalog.consultation.mapper.MedicalConsultationTypeMapper;
import com.amachi.app.vitalia.medicalcatalog.consultation.repository.MedicalConsultationTypeRepository;
import com.amachi.app.vitalia.medicalcatalog.consultation.service.MedicalConsultationTypeService;
import com.amachi.app.vitalia.medicalcatalog.consultation.specification.MedicalConsultationTypeSpecification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Medical Consultation Type Service Implementation.
 */
@Service
@RequiredArgsConstructor
@Getter
@Transactional(readOnly = true)
public class MedicalConsultationTypeServiceImpl extends BaseService<MedicalConsultationType, MedicalConsultationTypeDto, MedicalConsultationTypeSearchDto> implements MedicalConsultationTypeService {

    private final MedicalConsultationTypeRepository repository;
    private final MedicalConsultationTypeMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected Specification<MedicalConsultationType> buildSpecification(MedicalConsultationTypeSearchDto searchDto) {
        return new MedicalConsultationTypeSpecification(searchDto);
    }

    @Override
    protected MedicalConsultationType mapToEntity(MedicalConsultationTypeDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(MedicalConsultationTypeDto dto, MedicalConsultationType existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected void publishCreatedEvent(MedicalConsultationType entity) {
        publishChanged(entity);
    }

    @Override
    protected void publishUpdatedEvent(MedicalConsultationType entity) {
        publishChanged(entity);
    }

    private void publishChanged(MedicalConsultationType entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(
                    new MedicalConsultationTypeChangedEvent(entity.getId(), entity.getCode(), entity.getName())
            );
        }
    }
}
