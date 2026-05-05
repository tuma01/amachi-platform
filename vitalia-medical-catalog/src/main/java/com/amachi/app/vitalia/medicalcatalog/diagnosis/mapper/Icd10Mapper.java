package com.amachi.app.vitalia.medicalcatalog.diagnosis.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.dto.Icd10Dto;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.entity.Icd10;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(config = BaseMapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface Icd10Mapper extends EntityDtoMapper<Icd10, Icd10Dto> {

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    Icd10 toEntity(Icd10Dto dto);
 
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(Icd10Dto dto, @MappingTarget Icd10 entity);

    @Override
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    Icd10Dto toDto(Icd10 entity);
}
