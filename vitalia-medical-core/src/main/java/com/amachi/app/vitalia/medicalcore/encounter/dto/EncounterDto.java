package com.amachi.app.vitalia.medicalcore.encounter.dto;

import com.amachi.app.core.common.enums.VisitTypeEnum;
import com.amachi.app.vitalia.medicalcore.common.enums.EncounterStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Schema(description = "Fecha y hora de cierre del encuentro", example = "2026-03-30T11:15:00Z")
    private OffsetDateTime endTime;

    @NotNull(message = "Tipo Encuentro {err.mandatory}")
    @Schema(description = "Tipo de acto (AMBULATORY, EMERGENCY, VIRTUAL)", example = "AMBULATORY")
    private VisitTypeEnum encounterType;

    @NotNull(message = "Estado {err.mandatory}")
    @Schema(description = "Ciclo de vida del acto clínico (PLANNED, IN_PROGRESS, COMPLETED, etc.)", example = "IN_PROGRESS")
    private EncounterStatus status;

    @Size(max = 50, message = "Nivel triage {err.max.length}")
    @Schema(description = "Nivel de Triage (Urgencia)", example = "LEVEL_1_EMERGENCY")
    private String triageLevel;

    @Size(max = 2000, message = "Motivo consulta {err.max.length}")
    @Schema(description = "Motivo de la consulta (Chief Complaint)", example = "Dolor abdominal agudo")
    private String chiefComplaint;

    @Size(max = 5000, message = "Síntomas {err.max.length}")
    @Schema(description = "Sintomatología reportada", example = "Náuseas, fiebre leve")
    private String symptoms;

    @Size(max = 5000, message = "Notas diagnósticas {err.max.length}")
    @Schema(description = "Notas diagnósticas preliminares o finales", example = "Sospecha de apendicitis")
    private String diagnosisNotes;

    @Size(max = 5000, message = "Plan de tratamiento {err.max.length}")
    @Schema(description = "Plan de tratamiento propuesto", example = "Observación y analgésicos")
    private String treatmentPlan;

    @Size(max = 5000, message = "Recomendaciones {err.max.length}")
    @Schema(description = "Recomendaciones al paciente", example = "Reposo absoluto")
    private String recommendations;

    @Size(max = 5000, message = "Notas clínicas {err.max.length}")
    @Schema(description = "Notas adicionales de la atención clínica", example = "Paciente cooperativo")
    private String clinicalNotes;

    @Schema(description = "Duración total del encuentro en minutos (Calculado)", example = "45", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer durationMinutes;

    @Schema(description = "ID del episodio de cuidado", example = "301")
    private Long episodeOfCareId;

    @Schema(description = "ID del expediente clínico vinculado", example = "102")
    private Long medicalHistoryId;
}
