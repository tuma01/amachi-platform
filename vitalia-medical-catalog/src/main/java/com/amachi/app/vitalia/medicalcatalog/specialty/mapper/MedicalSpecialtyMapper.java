package com.amachi.app.vitalia.medicalcatalog.specialty.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcatalog.specialty.dto.MedicalSpecialtyDto;
import com.amachi.app.vitalia.medicalcatalog.specialty.entity.MedicalSpecialty;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface MedicalSpecialtyMapper extends EntityDtoMapper<MedicalSpecialty, MedicalSpecialtyDto> {

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    MedicalSpecialty toEntity(MedicalSpecialtyDto dto);

    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(MedicalSpecialtyDto dto, @MappingTarget MedicalSpecialty entity);

    @Override
    MedicalSpecialtyDto toDto(MedicalSpecialty entity);
}
