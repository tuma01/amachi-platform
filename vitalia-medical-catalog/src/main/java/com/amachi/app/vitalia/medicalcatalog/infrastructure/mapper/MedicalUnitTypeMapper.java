package com.amachi.app.vitalia.medicalcatalog.infrastructure.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.dto.MedicalUnitTypeDto;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.entity.MedicalUnitType;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface MedicalUnitTypeMapper extends EntityDtoMapper<MedicalUnitType, MedicalUnitTypeDto> {

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    MedicalUnitType toEntity(MedicalUnitTypeDto dto);
 
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(MedicalUnitTypeDto dto, @MappingTarget MedicalUnitType entity);

    @Override
    @BeanMapping(unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
    MedicalUnitTypeDto toDto(MedicalUnitType entity);
}
