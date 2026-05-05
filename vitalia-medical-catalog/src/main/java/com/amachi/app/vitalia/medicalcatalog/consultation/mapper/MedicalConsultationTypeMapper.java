package com.amachi.app.vitalia.medicalcatalog.consultation.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcatalog.consultation.dto.MedicalConsultationTypeDto;
import com.amachi.app.vitalia.medicalcatalog.consultation.entity.MedicalConsultationType;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface MedicalConsultationTypeMapper extends EntityDtoMapper<MedicalConsultationType, MedicalConsultationTypeDto> {

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @Mapping(target = "specialty.id", source = "specialtyId")
    MedicalConsultationType toEntity(MedicalConsultationTypeDto dto);
 
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "specialty.id", source = "specialtyId")
    void updateEntityFromDto(MedicalConsultationTypeDto dto, @MappingTarget MedicalConsultationType entity);

    @Override
    @BeanMapping(unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
    @Mapping(target = "specialtyId", source = "specialty.id")
    @Mapping(target = "specialtyName", source = "specialty.name")
    MedicalConsultationTypeDto toDto(MedicalConsultationType entity);
}
