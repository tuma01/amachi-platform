package com.amachi.app.vitalia.medicalcore.medicalhistory.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.ActualIllnessDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.ActualIllness;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface ActualIllnessMapper extends EntityDtoMapper<ActualIllness, ActualIllnessDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "medicalHistory.id", source = "medicalHistoryId")
    ActualIllness toEntity(ActualIllnessDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "medicalHistoryId", source = "medicalHistory.id")
    ActualIllnessDto toDto(ActualIllness entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "medicalHistory.id", source = "medicalHistoryId")
    void updateEntityFromDto(ActualIllnessDto dto, @MappingTarget ActualIllness existing);
}
