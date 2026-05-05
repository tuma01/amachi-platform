package com.amachi.app.vitalia.medicalcatalog.procedure.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcatalog.procedure.dto.MedicalProcedureDto;
import com.amachi.app.vitalia.medicalcatalog.procedure.entity.MedicalProcedure;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(config = BaseMapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface MedicalProcedureMapper extends EntityDtoMapper<MedicalProcedure, MedicalProcedureDto> {

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    MedicalProcedure toEntity(MedicalProcedureDto dto);
 
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(MedicalProcedureDto dto, @MappingTarget MedicalProcedure entity);

    @Override
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    MedicalProcedureDto toDto(MedicalProcedure entity);
}
