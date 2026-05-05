package com.amachi.app.vitalia.medicalcatalog.identity.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcatalog.identity.dto.IdentificationTypeDto;
import com.amachi.app.vitalia.medicalcatalog.identity.entity.IdentificationType;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(config = BaseMapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface IdentificationTypeMapper extends EntityDtoMapper<IdentificationType, IdentificationTypeDto> {

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    IdentificationType toEntity(IdentificationTypeDto dto);
 
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(IdentificationTypeDto dto, @MappingTarget IdentificationType entity);

    @Override
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    IdentificationTypeDto toDto(IdentificationType entity);
}
