package com.amachi.app.vitalia.medicalcore.familyhistory.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.familyhistory.dto.FamilyHistoryDto;
import com.amachi.app.vitalia.medicalcore.familyhistory.entity.FamilyHistory;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface FamilyHistoryMapper extends EntityDtoMapper<FamilyHistory, FamilyHistoryDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "medicalHistory.id",  source = "medicalHistoryId")
    @Mapping(target = "hereditaryDiseases", ignore = true)
    FamilyHistory toEntity(FamilyHistoryDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "medicalHistoryId", source = "medicalHistory.id")
    FamilyHistoryDto toDto(FamilyHistory entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "medicalHistory.id",  source = "medicalHistoryId")
    @Mapping(target = "hereditaryDiseases", ignore = true)
    void updateEntityFromDto(FamilyHistoryDto dto, @MappingTarget FamilyHistory existing);
}
