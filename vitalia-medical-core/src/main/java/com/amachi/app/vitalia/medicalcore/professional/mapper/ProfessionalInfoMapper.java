package com.amachi.app.vitalia.medicalcore.professional.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.professional.dto.ProfessionalInfoDto;
import com.amachi.app.vitalia.medicalcore.professional.entity.ProfessionalInfo;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface ProfessionalInfoMapper extends EntityDtoMapper<ProfessionalInfo, ProfessionalInfoDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "person", ignore = true)
    ProfessionalInfo toEntity(ProfessionalInfoDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "personId", source = "person.id")
    ProfessionalInfoDto toDto(ProfessionalInfo entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "person", ignore = true)
    void updateEntityFromDto(ProfessionalInfoDto dto, @MappingTarget ProfessionalInfo existing);
}
