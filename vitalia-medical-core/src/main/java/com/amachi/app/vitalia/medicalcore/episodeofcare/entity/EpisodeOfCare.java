package com.amachi.app.vitalia.medicalcore.episodeofcare.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcore.common.enums.EpisodeOfCareStatus;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Condition;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Encounter;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Agrupador macro de eventos clínicos (Ginecología - Embarazo, Oncología -
 * Tratamiento).
 * Gestiona el ciclo de vida de un cuidado continuo según el estándar FHIR
 * EpisodeOfCare (SaaS Elite Tier).
 */
@Entity
@Table(name = "MED_EPISODE_OF_CARE", indexes = {
        @Index(name = "IDX_EPI_TENANT", columnList = "TENANT_ID"),
        @Index(name = "IDX_EPI_STATUS", columnList = "STATUS"),
        @Index(name = "IDX_EPI_PATIENT", columnList = "FK_ID_PATIENT")
})
@Getter
@SuperBuilder
@Setter
@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode(callSuper = true, exclude = { "relatingConditions", "encounters" })
@Schema(description = "Gestión de episodio de cuidados prolongados o crónicos (FHIR EpisodeOfCare)")
@Audited
public class EpisodeOfCare extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_PATIENT", nullable = false, foreignKey = @ForeignKey(name = "FK_MED_EPISODE_OF_CARE_PATIENT"))
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_MANAGING_DOCTOR", nullable = false, foreignKey = @ForeignKey(name = "FK_MED_EPISODE_OF_CARE_DOCTOR"))
    private Doctor managingDoctor;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 30)
    private EpisodeOfCareStatus status;

    @Column(name = "TYPE", length = 100)
    private String typeDescription;

    @Column(name = "PERIOD_START", nullable = false)
    private OffsetDateTime periodStart;

    @Column(name = "PERIOD_END")
    private OffsetDateTime periodEnd;

    @OneToMany(mappedBy = "episodeOfCare", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Condition> relatingConditions = new ArrayList<>();

    @OneToMany(mappedBy = "episodeOfCare", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Encounter> encounters = new ArrayList<>();

    @Column(name = "GOALS", columnDefinition = "TEXT")
    private String goals;

    @Column(name = "NOTES", columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (this.typeDescription != null)
            this.typeDescription = this.typeDescription.trim().toUpperCase();
        if (this.goals != null)
            this.goals = this.goals.trim();
        if (this.periodStart == null)
            this.periodStart = OffsetDateTime.now();
    }
}
