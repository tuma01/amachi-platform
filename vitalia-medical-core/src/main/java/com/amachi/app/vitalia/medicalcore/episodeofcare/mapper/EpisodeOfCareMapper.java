package com.amachi.app.vitalia.medicalcore.episodeofcare.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.episodeofcare.dto.EpisodeOfCareDto;
import com.amachi.app.vitalia.medicalcore.episodeofcare.entity.EpisodeOfCare;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface EpisodeOfCareMapper extends EntityDtoMapper<EpisodeOfCare, EpisodeOfCareDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient.id",       source = "patientId")
    @Mapping(target = "managingDoctor.id", source = "managingDoctorId")
    @Mapping(target = "relatingConditions", ignore = true)
    @Mapping(target = "encounters",         ignore = true)
    EpisodeOfCare toEntity(EpisodeOfCareDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patientId",              source = "patient.id")
    @Mapping(target = "patientFullName",         expression = "java(entity.getPatient() != null && entity.getPatient().getPerson() != null ? entity.getPatient().getPerson().getFirstName() + \" \" + (entity.getPatient().getPerson().getLastName() != null ? entity.getPatient().getPerson().getLastName() : \"\") : null)")
    @Mapping(target = "managingDoctorId",        source = "managingDoctor.id")
    @Mapping(target = "managingDoctorFullName",  expression = "java(entity.getManagingDoctor() != null && entity.getManagingDoctor().getPerson() != null ? entity.getManagingDoctor().getPerson().getFirstName() + \" \" + (entity.getManagingDoctor().getPerson().getLastName() != null ? entity.getManagingDoctor().getPerson().getLastName() : \"\") : null)")
    EpisodeOfCareDto toDto(EpisodeOfCare entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient.id",         source = "patientId")
    @Mapping(target = "managingDoctor.id",  source = "managingDoctorId")
    @Mapping(target = "relatingConditions", ignore = true)
    @Mapping(target = "encounters",         ignore = true)
    void updateEntityFromDto(EpisodeOfCareDto dto, @MappingTarget EpisodeOfCare existing);
}
