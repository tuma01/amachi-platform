package com.amachi.app.vitalia.medicalcore.encounter.dto;

import com.amachi.app.core.common.enums.VisitTypeEnum;
import com.amachi.app.vitalia.medicalcore.common.enums.EncounterStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.OffsetDateTime;

/**
 * Esquema unificado para la transferencia de información de encuentros clínicos (FHIR Encounter).
 * Representa cualquier interacción médica directa o indirecta.
 */
@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Encounter", description = "Esquema integral de interacción clínica (Grado Hospitalario / FHIR Compliant)")
public class EncounterDto {

    @Schema(description = "Identificador único interno (PK)", example = "12001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Identificador Global del Encuentro (Público/FHIR)", example = "ENC-7890-UUID", accessMode = Schema.AccessMode.READ_ONLY)
    private String externalId;

    @NotNull(message = "Paciente {err.mandatory}")
    @Schema(description = "ID del paciente atendido", example = "5001")
    private Long patientId;

    @Schema(description = "Nombre completo del paciente (Solo lectura)", example = "JUAN CARLOS PEREZ", accessMode = Schema.AccessMode.READ_ONLY)
    private String patientFullName;

    @NotNull(message = "Médico {err.mandatory}")
    @Schema(description = "ID del facultativo responsable", example = "2001")
    private Long doctorId;

    @Schema(description = "Nombre completo del médico (Solo lectura)", example = "DR. MARCOS SOLIZ", accessMode = Schema.AccessMode.READ_ONLY)
    private String doctorFullName;

    @Schema(description = "ID de la cita origen (Logística)", example = "9001")
    private Long appointmentId;

//    @Schema(description = "ID del tipo de consulta", example = "1")
//    private Long consultationTypeId;

    @NotNull(message = "Fecha Encuentro {err.mandatory}")
    @Schema(description = "Fecha y hora real del encuentro clínico (ISO-8601)", example = "2026-03-30T10:30:00Z")
    private OffsetDateTime encounterDate;

    @NotNull(message = "Tipo Encuentro {err.mandatory}")
    @Schema(description = "Tipo de acto (AMBULATORY, EMERGENCY, VIRTUAL)", example = "AMBULATORY")
    private VisitTypeEnum encounterType;

    @NotNull(message = "Estado {err.mandatory}")
    @Schema(description = "Ciclo de vida del acto clínico (PLANNED, IN_PROGRESS, COMPLETED, etc.)", example = "IN_PROGRESS")
    private EncounterStatus status;

    @Schema(description = "Nivel de Triage (Urgencia)", example = "LEVEL_1_EMERGENCY")
    private String triageLevel;

    @Schema(description = "Motivo de la consulta (Chief Complaint)", example = "Dolor abdominal agudo")
    private String chiefComplaint;

    @Schema(description = "Sintomatología reportada", example = "Náuseas, fiebre leve")
    private String symptoms;

    @Schema(description = "Notas diagnósticas preliminares o finales", example = "Sospecha de apendicitis")
    private String diagnosisNotes;

    @Schema(description = "Plan de tratamiento propuesto", example = "Observación y analgésicos")
    private String treatmentPlan;

    @Schema(description = "Recomendaciones al paciente", example = "Reposo absoluto")
    private String recommendations;

    @Schema(description = "Notas adicionales de la atención clínica", example = "Paciente cooperativo")
    private String clinicalNotes;

    @Schema(description = "Duración total del encuentro en minutos (Calculado)", example = "45", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer durationMinutes;

    @Schema(description = "ID del episodio de cuidado", example = "301")
    private Long episodeOfCareId;

//    @NotNull(message = "Medical History {err.mandatory}")
//    @Schema(description = "ID del expediente clínico vinculado", example = "102")
//    private Long medicalHistoryId;
}
