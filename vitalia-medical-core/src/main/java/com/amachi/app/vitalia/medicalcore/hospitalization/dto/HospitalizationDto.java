package com.amachi.app.vitalia.medicalcore.hospitalization.dto;

import com.amachi.app.vitalia.medicalcore.common.enums.AdmissionType;
import com.amachi.app.vitalia.medicalcore.common.enums.DischargeStatus;
import com.amachi.app.vitalia.medicalcore.common.enums.HospitalizationPriority;
import com.amachi.app.vitalia.medicalcore.common.enums.HospitalizationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Hospitalization", description = "Episodio de internación hospitalaria (SaaS Elite Tier)")
public class HospitalizationDto {

    @Schema(description = "Identificador único interno (PK)", example = "8001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Identificador Global del Episodio (FHIR)", example = "HOSP-990-UUID", accessMode = Schema.AccessMode.READ_ONLY)
    private String externalId;

    @NotNull(message = "Paciente {err.mandatory}")
    @Schema(description = "ID del paciente internado", example = "5001")
    private Long patientId;

    @Schema(description = "Nombre completo del paciente (Solo lectura)", example = "JUAN PEREZ", accessMode = Schema.AccessMode.READ_ONLY)
    private String patientFullName;

    @Schema(description = "ID del encuentro clínico origen", example = "3001")
    private Long encounterId;

    @NotNull(message = "Médico responsable {err.mandatory}")
    @Schema(description = "ID del médico responsable", example = "2001")
    private Long doctorId;

    @Schema(description = "Nombre del médico (Solo lectura)", example = "DR. MARCOS SOLIZ", accessMode = Schema.AccessMode.READ_ONLY)
    private String doctorFullName;

    @Schema(description = "ID de la enfermera responsable de turno", example = "4001")
    private Long nurseId;

    @Schema(description = "Nombre de la enfermera (Solo lectura)", example = "LIC. ANA FLORES", accessMode = Schema.AccessMode.READ_ONLY)
    private String nurseFullName;

    @Schema(description = "ID de la unidad hospitalaria asignada", example = "101")
    private Long unitId;

    @Schema(description = "Nombre de la unidad (Solo lectura)", example = "UCI — ALA NORTE", accessMode = Schema.AccessMode.READ_ONLY)
    private String unitName;

    @Schema(description = "ID de la habitación asignada", example = "202")
    private Long roomId;

    @Schema(description = "Número de habitación (Solo lectura)", example = "304-A", accessMode = Schema.AccessMode.READ_ONLY)
    private String roomNumber;

    @Schema(description = "ID de la cama asignada", example = "505")
    private Long bedId;

    @Schema(description = "Código de la cama (Solo lectura)", example = "CAMA-01-B", accessMode = Schema.AccessMode.READ_ONLY)
    private String bedCode;

    @NotNull(message = "Tipo de admisión {err.mandatory}")
    @Schema(description = "Tipo de ingreso (EMERGENCY, PLANNED, OBSERVATION, OUTPATIENT)", example = "EMERGENCY")
    private AdmissionType admissionType;

    @Schema(description = "Prioridad clínica del ingreso", example = "HIGH")
    private HospitalizationPriority priority;

    @NotNull(message = "Estado {err.mandatory}")
    @Schema(description = "Estado operativo del episodio de internación", example = "ADMITTED")
    private HospitalizationStatus status;

    @Schema(description = "Clasificación del alta médica", example = "RECOVERED")
    private DischargeStatus dischargeStatus;

    @NotNull(message = "Fecha de admisión {err.mandatory}")
    @Schema(description = "Fecha y hora de ingreso hospitalario", example = "2026-03-24T10:00:00Z")
    private OffsetDateTime admissionDate;

    @Schema(description = "Fecha estimada de alta (Planificación)", example = "2026-03-28T14:30:00Z")
    private OffsetDateTime estimatedDischargeDate;

    @Schema(description = "Fecha real del alta médica", example = "2026-03-28T14:30:00Z")
    private OffsetDateTime dischargeDate;

    @Schema(description = "Fecha de seguimiento post-alta", example = "2026-04-05")
    private LocalDate followUpDate;

    @Schema(description = "Motivo de ingreso", example = "DOLOR ABDOMINAL AGUDO")
    private String admissionReason;

    @Schema(description = "Diagnóstico de ingreso", example = "APENDICITIS AGUDA")
    private String admissionDiagnosis;

    @Schema(description = "Diagnóstico final tras estudios", example = "APENDICITIS SUPURADA")
    private String finalDiagnosis;

    @Schema(description = "Plan de tratamiento durante la estancia")
    private String treatmentPlan;

    @Schema(description = "Motivo del alta médica")
    private String dischargeReason;

    @Schema(description = "Instrucciones de alta para el paciente")
    private String dischargeInstructions;

    @Schema(description = "Observaciones clínicas adicionales")
    private String observations;

    @Schema(description = "Número de autorización del seguro", example = "AUTH-12345-XYZ")
    private String insuranceAuthorizationNumber;

    @Schema(description = "Costo acumulado (Solo lectura)", example = "1500.50", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal totalCost;

    @Schema(description = "Moneda del costo", example = "USD")
    private String currency;

    @Schema(description = "Días de estancia calculados (Solo lectura)", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private Long lengthOfStayInDays;
}
