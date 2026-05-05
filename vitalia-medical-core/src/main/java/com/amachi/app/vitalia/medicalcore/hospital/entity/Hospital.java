package com.amachi.app.vitalia.medicalcore.hospital.entity;

import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Entidad especializada para perfiles de Hospitales (SaaS Elite Tier).
 * Hospital IS un Tenant (Platform-as-a-Tenant).
 * Sigue la estrategia JOINED: los campos base (code, name, auditoría)
 * residen en DMN_TENANT. Aquí solo van los campos médicos específicos.
 *
 * FK_ID_PERSON referencia la identidad legal del hospital (Golden Protocol).
 */
@Entity
@Table(name = "MED_HOSPITAL")
@PrimaryKeyJoinColumn(name = "TENANT_ID")
@DiscriminatorValue("HOSPITAL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Hospital extends Tenant {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_PERSON", foreignKey = @ForeignKey(name = "FK_HOSPITAL_PERSON"))
    private Person person;

    @Column(name = "LEGAL_NAME", length = 200, nullable = false)
    private String legalName;

    @Column(name = "TAX_ID", length = 50, nullable = false)
    private String taxId;

    @Column(name = "MEDICAL_LICENSE", length = 100, nullable = false)
    private String medicalLicense;

    @Column(name = "CONTACT_PHONE", length = 50)
    private String contactPhone;

    @Column(name = "CONTACT_EMAIL", length = 100)
    private String contactEmail;

    @Column(name = "WEBSITE", length = 255)
    private String website;

    // --- Pro Fields (Fase 13) ---

    @Column(name = "MEDICAL_DIRECTOR", length = 150)
    private String medicalDirector;

    @Column(name = "MEDICAL_DIRECTOR_LICENSE", length = 100)
    private String medicalDirectorLicense;

    @Column(name = "HOSPITAL_CATEGORY", length = 100)
    private String hospitalCategory;

    @Column(name = "BED_CAPACITY")
    private Integer bedCapacity;

    @Column(name = "OPERATING_ROOMS_COUNT")
    private Integer operatingRoomsCount;

    @Column(name = "EMERGENCY_247")
    private Boolean emergency247;

    @Column(name = "SLOGAN", length = 255)
    private String slogan;

    @Column(name = "FAX_NUMBER", length = 50)
    private String faxNumber;

    @Column(name = "WHATSAPP_NUMBER", length = 50)
    private String whatsappNumber;

    @Column(name = "SOCIAL_LINKS", columnDefinition = "TEXT")
    private String socialLinks;

    @Column(name = "SEAL_URL", length = 255)
    private String sealUrl;

    // ==========================================
    // Lógica de Normalización (Elite Standard)
    // ==========================================

    @PrePersist
    @PreUpdate
    private void normalizeHospital() {
        if (this.taxId != null) {
            this.taxId = this.taxId.trim().toUpperCase();
        }
        if (this.legalName != null) {
            this.legalName = this.legalName.trim();
        }
    }
}
