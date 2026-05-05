package com.amachi.app.vitalia.medicalcatalog.healthcareprovider.entity;

import com.amachi.app.core.common.entity.Auditable;
import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.geography.address.entity.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Maestro de Proveedores de Salud (Aseguradoras/Pagadores).
 * Gestionada por el SuperAdmin como catálogo global compartido.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "CAT_HEALTHCARE_PROVIDER",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_PROVIDER_CODE", columnNames = {"CODE"}),
        @UniqueConstraint(name = "UK_PROVIDER_TAX_ID", columnNames = {"TAX_ID"})
    },
    indexes = {
        @Index(name = "IDX_PROVIDER_CODE", columnList = "CODE"),
        @Index(name = "IDX_PROVIDER_TAX", columnList = "TAX_ID")
    }
)
public class HealthcareProvider extends Auditable<String> implements Model {

    @NotBlank(message = "Code {err.mandatory}")
    @Column(name = "CODE", nullable = false, length = 20)
    private String code;

    /** Razón social oficial o nombre comercial reconocido. */
    @NotBlank(message = "Name {err.mandatory}")
    @Column(name = "NAME", nullable = false, length = 250)
    private String name;

    /** Número de Identificación Tributaria o Registro Fiscal Institucional. */
    @NotBlank(message = "Tax ID {err.mandatory}")
    @Column(name = "TAX_ID", nullable = false, length = 50)
    private String taxId;

    /** Correo electrónico institucional para la coordinación de servicios de salud. */
    @Column(name = "OFFICIAL_EMAIL", length = 100)
    private String officialEmail;

    /** Número telefónico de la central de autorizaciones o conmutador principal. */
    @Column(name = "OFFICIAL_PHONE", length = 50)
    private String officialPhone;

    /** Línea nacional de emergencias habilitada las 24 horas para el paciente. */
    @Column(name = "EMERGENCY_PHONE", length = 50)
    private String emergencyPhone;

    /** Portal institucional oficial o plataforma de validación de derechos. */
    @Column(name = "WEBSITE", length = 155)
    private String website;

    /** Ruta del activo digital (logo) para representación gráfica en el sistema. */
    @Column(name = "LOGO_URL", length = 255)
    private String logoUrl;

    /** Ubicación física de la casa matriz o sede administrativa nacional. */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_HQ_ADDRESS", foreignKey = @ForeignKey(name = "FK_PROVIDER_HQ_ADDRESS"))
    private Address hqAddress;

    @Column(name = "IS_ACTIVE", nullable = false)
    @Builder.Default
    private boolean active = true;

    // ==========================================
    // 🧠 Lógica de Normalización (Elite Standard)
    // ==========================================

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (this.code != null) this.code = this.code.trim().toUpperCase();
        if (this.name != null) this.name = this.name.trim();
        if (this.taxId != null) this.taxId = this.taxId.trim().toUpperCase();
    }
}
