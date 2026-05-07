package com.amachi.app.vitalia.medicalcore.insurance.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.insurance.dto.InsuranceDto;
import com.amachi.app.vitalia.medicalcore.insurance.entity.Insurance;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface InsuranceMapper extends EntityDtoMapper<Insurance, InsuranceDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "medicalHistory.id", source = "medicalHistoryId")
    @Mapping(target = "provider.id",       source = "providerId")
    Insurance toEntity(InsuranceDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "medicalHistoryId", source = "medicalHistory.id")
    @Mapping(target = "providerId",       source = "provider.id")
    @Mapping(target = "providerName",     source = "provider.name")
    InsuranceDto toDto(Insurance entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "medicalHistory.id", source = "medicalHistoryId")
    @Mapping(target = "provider.id",       source = "providerId")
    void updateEntityFromDto(InsuranceDto dto, @MappingTarget Insurance existing);
}
