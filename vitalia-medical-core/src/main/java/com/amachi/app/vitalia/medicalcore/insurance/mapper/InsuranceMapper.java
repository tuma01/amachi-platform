package com.amachi.app.vitalia.medicalcore.insurance.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.insurance.dto.InsuranceDto;
import com.amachi.app.vitalia.medicalcore.insurance.entity.Insurance;
import org.mapstruct.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Mapping(target = "medicalHistoryId",      source = "medicalHistory.id")
    @Mapping(target = "providerId",            source = "provider.id")
    @Mapping(target = "providerName",          source = "provider.name")
    @Mapping(target = "providerPhone",         source = "provider.officialPhone")
    @Mapping(target = "providerEmergencyPhone",source = "provider.emergencyPhone")
    @Mapping(target = "providerEmail",         source = "provider.officialEmail")
    @Mapping(target = "providerWebsite",       source = "provider.website")
    InsuranceDto toDto(Insurance entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "medicalHistory.id", source = "medicalHistoryId")
    @Mapping(target = "provider.id",       source = "providerId")
    void updateEntityFromDto(InsuranceDto dto, @MappingTarget Insurance existing);

    @AfterMapping
    default void resolveReadOnlyFields(Insurance source, @MappingTarget InsuranceDto target) {
        if (source.getMedicalHistory() != null) {
            target.setHistoryNumber(source.getMedicalHistory().getHistoryNumber());
            var patient = source.getMedicalHistory().getPatient();
            if (patient != null && patient.getPerson() != null) {
                var person = patient.getPerson();
                String fullName = Stream.of(person.getFirstName(), person.getLastName())
                        .filter(s -> s != null && !s.isBlank())
                        .collect(Collectors.joining(" "));
                target.setPatientFullName(fullName.isBlank() ? null : fullName);
            }
        }
    }
}
