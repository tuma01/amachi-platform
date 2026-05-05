package com.amachi.app.vitalia.medicalcatalog.healthcareprovider.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.dto.HealthcareProviderDto;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.entity.HealthcareProvider;
import org.mapstruct.*;

import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        config = BaseMapperConfig.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true)
)
public interface HealthcareProviderMapper extends EntityDtoMapper<HealthcareProvider, HealthcareProviderDto> {

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @Mapping(target = "hqAddress.id", source = "hqAddressId")
    HealthcareProvider toEntity(HealthcareProviderDto dto);

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "hqAddress.id", source = "hqAddressId")
    void updateEntityFromDto(HealthcareProviderDto dto, @MappingTarget HealthcareProvider entity);

    @Override
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    @Mapping(
            target = "hqAddressId",
            expression = "java(entity.getHqAddress() != null ? entity.getHqAddress().getId() : null)"
    )
    HealthcareProviderDto toDto(HealthcareProvider entity);
}
