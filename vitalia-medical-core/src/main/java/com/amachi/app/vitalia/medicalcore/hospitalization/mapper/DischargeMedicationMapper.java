package com.amachi.app.vitalia.medicalcore.hospitalization.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.DischargeMedicationDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.entity.DischargeMedication;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface DischargeMedicationMapper extends EntityDtoMapper<DischargeMedication, DischargeMedicationDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "hospitalization", ignore = true)
    @Mapping(target = "medication",      ignore = true)
    DischargeMedication toEntity(DischargeMedicationDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "hospitalizationId",   source = "hospitalization.id")
    @Mapping(target = "medicationId",        source = "medication.id")
    @Mapping(target = "medicationCatalogName", source = "medication.genericName")
    DischargeMedicationDto toDto(DischargeMedication entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "hospitalization", ignore = true)
    @Mapping(target = "medication",      ignore = true)
    void updateEntityFromDto(DischargeMedicationDto dto, @MappingTarget DischargeMedication entity);
}
