package com.amachi.app.core.management.tenantconfig.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.core.management.tenantconfig.dto.TenantConfigDto;
import com.amachi.app.core.management.tenantconfig.entity.TenantConfig;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, uses = {com.amachi.app.core.geography.address.mapper.AddressMapper.class}, builder = @Builder(disableBuilder = true))
public interface TenantConfigMapper extends EntityDtoMapper<TenantConfig, TenantConfigDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "tenant.id", source = "tenantId")
    @Mapping(target = "tenant.name", source = "tenantName")
    @Mapping(target = "tenant.description", source = "tenantDescription")
    @Mapping(target = "tenant.logoUrl", source = "logoUrl")
    @Mapping(target = "tenant.faviconUrl", source = "faviconUrl")
    TenantConfig toEntity(TenantConfigDto dto);

    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "tenant.id", source = "tenantId")
    @Mapping(target = "tenant.name", source = "tenantName")
    @Mapping(target = "tenant.description", source = "tenantDescription")
    @Mapping(target = "tenant.logoUrl", source = "logoUrl")
    @Mapping(target = "tenant.faviconUrl", source = "faviconUrl")
    void updateEntityFromDto(TenantConfigDto dto, @MappingTarget TenantConfig entity);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
    @Mapping(target = "tenantId", source = "tenant.id")
    @Mapping(target = "tenantName", source = "tenant.name")
    @Mapping(target = "tenantDescription", source = "tenant.description")
    @Mapping(target = "logoUrl", source = "tenant.logoUrl")
    @Mapping(target = "faviconUrl", source = "tenant.faviconUrl")
    TenantConfigDto toDto(TenantConfig entity);

    /* 
    @AfterMapping
    default void mapProfessionalData(TenantConfig entity, @MappingTarget TenantConfigDto dto) {
        if (entity.getTenant() instanceof Hospital hospital) {
            dto.setLegalName(hospital.getLegalName());
            dto.setTaxId(hospital.getTaxId());
            dto.setMedicalLicense(hospital.getMedicalLicense());
            dto.setContactPhone(hospital.getContactPhone());
            dto.setContactEmail(hospital.getContactEmail());
            dto.setWebsite(hospital.getWebsite());

            // Pro Fields (Fase 13)
            dto.setMedicalDirector(hospital.getMedicalDirector());
            dto.setMedicalDirectorLicense(hospital.getMedicalDirectorLicense());
            dto.setHospitalCategory(hospital.getHospitalCategory());
            dto.setBedCapacity(hospital.getBedCapacity());
            dto.setOperatingRoomsCount(hospital.getOperatingRoomsCount());
            dto.setEmergency247(hospital.getEmergency247());
            dto.setSlogan(hospital.getSlogan());
            dto.setFaxNumber(hospital.getFaxNumber());
            dto.setWhatsappNumber(hospital.getWhatsappNumber());
            dto.setSocialLinks(hospital.getSocialLinks());
            dto.setSealUrl(hospital.getSealUrl());
        }
    }

    @AfterMapping
    default void updateProfessionalEntity(TenantConfigDto dto, @MappingTarget TenantConfig entity) {
        if (entity.getTenant() instanceof Hospital hospital) {
            hospital.setLegalName(dto.getLegalName());
            hospital.setTaxId(dto.getTaxId());
            hospital.setMedicalLicense(dto.getMedicalLicense());
            hospital.setContactPhone(dto.getContactPhone());
            hospital.setContactEmail(dto.getContactEmail());
            hospital.setWebsite(dto.getWebsite());

            // Pro Fields (Fase 13)
            hospital.setMedicalDirector(dto.getMedicalDirector());
            hospital.setMedicalDirectorLicense(dto.getMedicalDirectorLicense());
            hospital.setHospitalCategory(dto.getHospitalCategory());
            hospital.setBedCapacity(dto.getBedCapacity());
            hospital.setOperatingRoomsCount(dto.getOperatingRoomsCount());
            hospital.setEmergency247(dto.getEmergency247());
            hospital.setSlogan(dto.getSlogan());
            hospital.setFaxNumber(dto.getFaxNumber());
            hospital.setWhatsappNumber(dto.getWhatsappNumber());
            hospital.setSocialLinks(dto.getSocialLinks());
            hospital.setSealUrl(dto.getSealUrl());
        }
    }
    */
}
