package com.amachi.app.vitalia.medicalcore.familyhistory.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.familyhistory.dto.HereditaryDiseaseDto;
import com.amachi.app.vitalia.medicalcore.familyhistory.entity.HereditaryDisease;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface HereditaryDiseaseMapper extends EntityDtoMapper<HereditaryDisease, HereditaryDiseaseDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "familyHistory.id", source = "familyHistoryId")
    @Mapping(target = "kinship.id",       source = "kinshipId")
    HereditaryDisease toEntity(HereditaryDiseaseDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "familyHistoryId", source = "familyHistory.id")
    @Mapping(target = "kinshipId",       source = "kinship.id")
    @Mapping(target = "kinshipName",     source = "kinship.name")
    HereditaryDiseaseDto toDto(HereditaryDisease entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "familyHistory.id", source = "familyHistoryId")
    @Mapping(target = "kinship.id",       source = "kinshipId")
    void updateEntityFromDto(HereditaryDiseaseDto dto, @MappingTarget HereditaryDisease existing);
}
