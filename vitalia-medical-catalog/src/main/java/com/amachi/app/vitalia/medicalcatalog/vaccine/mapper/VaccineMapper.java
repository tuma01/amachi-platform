package com.amachi.app.vitalia.medicalcatalog.vaccine.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcatalog.vaccine.dto.VaccineDto;
import com.amachi.app.vitalia.medicalcatalog.vaccine.entity.Vaccine;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(config = BaseMapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface VaccineMapper extends EntityDtoMapper<Vaccine, VaccineDto> {

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    Vaccine toEntity(VaccineDto dto);
 
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(VaccineDto dto, @MappingTarget Vaccine entity);

    @Override
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    VaccineDto toDto(Vaccine entity);
}
