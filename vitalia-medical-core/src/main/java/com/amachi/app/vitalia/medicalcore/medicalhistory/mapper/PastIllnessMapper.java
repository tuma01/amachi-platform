package com.amachi.app.vitalia.medicalcore.medicalhistory.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.PastIllnessDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.PastIllness;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface PastIllnessMapper extends EntityDtoMapper<PastIllness, PastIllnessDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "medicalHistory.id", source = "medicalHistoryId")
    PastIllness toEntity(PastIllnessDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "medicalHistoryId", source = "medicalHistory.id")
    PastIllnessDto toDto(PastIllness entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "medicalHistory.id", source = "medicalHistoryId")
    void updateEntityFromDto(PastIllnessDto dto, @MappingTarget PastIllness existing);
}
