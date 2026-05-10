package com.amachi.app.core.auth.auditevent.revision;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

/**
 * Entidad de revisión Envers extendida.
 * Registra quién realizó cada cambio auditado, cuándo y en qué tenant.
 *
 * Tabla: DMN_AUDIT_REVISION (renombrada de REVINFO para seguir la convención DMN_)
 * ISO 27001 A.12.4 / HIPAA §164.312(b) — trazabilidad completa de cambios.
 *
 * Campos heredados del estándar Envers:
 *   REV      → número de revisión (PK autoincremental)
 *   REVTSTMP → timestamp Unix de la revisión
 *
 * Campos extendidos:
 *   USER_ID     → ID del usuario que realizó el cambio
 *   TENANT_CODE → tenant en el que ocurrió el cambio
 *   USERNAME    → username del actor (para trazabilidad sin JOIN)
 */
@Entity
@Table(name = "DMN_AUDIT_REVISION")
@RevisionEntity(AuditRevisionListener.class)
@Getter
@Setter
public class AuditRevisionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @Column(name = "REV")
    private long rev;

    @RevisionTimestamp
    @Column(name = "REVTSTMP")
    private long revtstmp;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "TENANT_CODE", length = 50)
    private String tenantCode;

    @Column(name = "USERNAME", length = 100)
    private String username;
}
