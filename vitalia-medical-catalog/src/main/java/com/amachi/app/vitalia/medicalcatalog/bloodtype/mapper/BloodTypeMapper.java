package com.amachi.app.vitalia.medicalcatalog.bloodtype.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.dto.BloodTypeDto;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.entity.BloodType;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface BloodTypeMapper extends EntityDtoMapper<BloodType, BloodTypeDto> {

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    BloodType toEntity(BloodTypeDto dto);
 
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(BloodTypeDto dto, @MappingTarget BloodType entity);

    @Override
    @BeanMapping(unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
    BloodTypeDto toDto(BloodType entity);
}
