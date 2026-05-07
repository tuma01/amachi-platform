package com.amachi.app.vitalia.medicalcore.habit.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.habit.dto.PhysiologicalHabitDto;
import com.amachi.app.vitalia.medicalcore.habit.entity.PhysiologicalHabit;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface PhysiologicalHabitMapper extends EntityDtoMapper<PhysiologicalHabit, PhysiologicalHabitDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "medicalHistory.id", source = "medicalHistoryId")
    PhysiologicalHabit toEntity(PhysiologicalHabitDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "medicalHistoryId", source = "medicalHistory.id")
    PhysiologicalHabitDto toDto(PhysiologicalHabit entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "medicalHistory.id", source = "medicalHistoryId")
    void updateEntityFromDto(PhysiologicalHabitDto dto, @MappingTarget PhysiologicalHabit existing);
}
