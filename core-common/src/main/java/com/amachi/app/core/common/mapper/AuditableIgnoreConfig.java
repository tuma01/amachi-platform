package com.amachi.app.core.common.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;

/**
 * Global Configuration for MapStruct to ignore infrastructure fields.
 * This class defines custom annotations to be used in mappers to maintain a clean contract.
 */
@MapperConfig
public interface AuditableIgnoreConfig {
    /**
     * 🟢 1. PARA ENTIDADES GLOBALES PURAS (Catálogos: CIE-10, País, Alergia, etc.)
     * Solo ignora campos de auditoría base y externalId. No incluye campos de tenant.
     */
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @interface IgnorePureAuditableFields {}

    /**
     * 🟡 2. PARA ENTIDADES GLOBALES HÍBRIDAS (Identidad: User, Role, Tenant, Theme)
     * Ignora auditoría, externalId Y los campos de compatibilidad (tenantId/Code).
     */
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "tenantCode", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @interface IgnoreHybridAuditableFields {}

    /**
     * 🔴 3. PARA ENTIDADES SCOPED (Pacientes, Citas, UserAccount, etc.)
     * Ignora auditoría y metadatos, pero el tenantId suele ser gestionado por el flujo.
     */
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "tenantCode", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @interface IgnoreTenantAuditableFields {}

    @Mapping(target = "isDeleted", ignore = true)
    @interface IgnoreSoftDelete {}
}
