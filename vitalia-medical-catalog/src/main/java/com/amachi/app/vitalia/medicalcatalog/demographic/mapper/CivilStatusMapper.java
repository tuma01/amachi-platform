package com.amachi.app.vitalia.medicalcatalog.demographic.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcatalog.demographic.dto.CivilStatusDto;
import com.amachi.app.vitalia.medicalcatalog.demographic.entity.CivilStatus;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(config = BaseMapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface CivilStatusMapper extends EntityDtoMapper<CivilStatus, CivilStatusDto> {

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    CivilStatus toEntity(CivilStatusDto dto);
 
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CivilStatusDto dto, @MappingTarget CivilStatus entity);

    @Override
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    CivilStatusDto toDto(CivilStatus entity);
}
