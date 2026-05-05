package com.amachi.app.vitalia.medicalcatalog.allergy.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcatalog.allergy.dto.AllergyDto;
import com.amachi.app.vitalia.medicalcatalog.allergy.entity.Allergy;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface AllergyMapper extends EntityDtoMapper<Allergy, AllergyDto> {

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    Allergy toEntity(AllergyDto dto);

    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AllergyDto dto, @MappingTarget Allergy entity);

    @Override
    AllergyDto toDto(Allergy entity);
}
