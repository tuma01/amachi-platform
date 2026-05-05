package com.amachi.app.vitalia.medicalcatalog.medication.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcatalog.medication.dto.MedicationDto;
import com.amachi.app.vitalia.medicalcatalog.medication.entity.Medication;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MedicationMapper extends EntityDtoMapper<Medication, MedicationDto> {

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    Medication toEntity(MedicationDto dto);

    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(MedicationDto dto, @MappingTarget Medication entity);

    @Override
    MedicationDto toDto(Medication entity);
}
