package com.amachi.app.vitalia.medicalcatalog.kinship.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcatalog.kinship.dto.KinshipDto;
import com.amachi.app.vitalia.medicalcatalog.kinship.entity.Kinship;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(config = BaseMapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface KinshipMapper extends EntityDtoMapper<Kinship, KinshipDto> {

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    Kinship toEntity(KinshipDto dto);
 
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(KinshipDto dto, @MappingTarget Kinship entity);

    @Override
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    KinshipDto toDto(Kinship entity);
}
