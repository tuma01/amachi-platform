package com.amachi.app.vitalia.medicalcore.observation.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcore.common.enums.ObservationStatus;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Encounter;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.time.OffsetDateTime;

/**
 * Clinical measurements (vital signs, lab results) aligned with FHIR Observation standard (SaaS Elite Tier).
 */
@Entity
@Table(
    name = "MED_OBSERVATION",
    indexes = {
        @Index(name = "IDX_OBS_TENANT", columnList = "TENANT_ID"),
        @Index(name = "IDX_OBS_PATIENT", columnList = "FK_ID_PATIENT"),
        @Index(name = "IDX_OBS_CODE", columnList = "OBS_CODE")
    }
)
@Getter
@SuperBuilder @Setter
@NoArgsConstructor @AllArgsConstructor

@EqualsAndHashCode(callSuper = true)
@Audited
@Schema(description = "Registro unificado de hallazgo o medición clínica (FHIR Observation)")
public class Observation extends AuditableTenantEntity implements Model {

    /**
     * Patient associated with the observation
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_PATIENT", nullable = false, foreignKey = @ForeignKey(name = "FK_OBSERVATION_PATIENT"))
    private Patient patient;

    /**
     * Related clinical encounter (optional)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_ENCOUNTER", foreignKey = @ForeignKey(name = "FK_OBSERVATION_ENCOUNTER"))
    private Encounter encounter;

    /**
     * Practitioner who recorded the observation
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_DOCTOR", nullable = false, foreignKey = @ForeignKey(name = "FK_OBSERVATION_DOCTOR"))
    private Doctor doctor;

    /**
     * Observation lifecycle status (FINAL, PRELIMINARY, etc.)
     */
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "STATUS", nullable = false, length = 30)
    private ObservationStatus status = ObservationStatus.FINAL;

    /**
     * Standard observation code (LOINC, SNOMED, etc.)
     */
    @Column(name = "OBS_CODE", nullable = false, length = 100)
    private String code;

    /**
     * Human-readable name
     */
    @Column(name = "DISPLAY_NAME", length = 200)
    private String name;

    /**
     * Observation value (textual or numeric)
     */
    @Column(name = "VALUE_TEXT", columnDefinition = "TEXT")
    private String value;

    /**
     * Unit of measure (UCUM)
     */
    @Column(name = "UNIT", length = 30)
    private String unit;

    /**
     * Reference range (e.g., 60-100 bpm)
     */
    @Column(name = "REFERENCE_RANGE", length = 100)
    private String referenceRange;

    /**
     * Clinical interpretation (HIGH, LOW, NORMAL)
     */
    @Column(name = "INTERPRETATION", length = 50)
    private String interpretation;

    /**
     * Effective date/time of the observation
     */
        @Column(name = "EFFECTIVE_DATETIME", nullable = false)
    private OffsetDateTime effectiveDateTime;

    /**
     * Additional notes
     */
    @Column(name = "NOTES", columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    private void onCreate() {
        if (this.effectiveDateTime == null) {
            this.effectiveDateTime = OffsetDateTime.now();
        }
        if (this.name != null) this.name = this.name.trim();
        if (this.code != null) this.code = this.code.trim().toUpperCase();
    }

    @PreUpdate
    private void onUpdate() {
        if (this.name != null) this.name = this.name.trim();
        if (this.code != null) this.code = this.code.trim().toUpperCase();
    }
}
