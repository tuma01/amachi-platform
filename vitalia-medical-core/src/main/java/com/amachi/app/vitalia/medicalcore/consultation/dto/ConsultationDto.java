package com.amachi.app.vitalia.medicalcore.consultation.dto;

import com.amachi.app.vitalia.medicalcore.common.enums.ConsultationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.OffsetDateTime;

@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Consultation", description = "Registro de consulta médica (SaaS Elite Tier)")
public class ConsultationDto {

    @Schema(description = "Identificador único interno (PK)", example = "7001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Identificador externo de idempotencia", example = "330e8400-e29b-41d4-a716-446655449999", accessMode = Schema.AccessMode.READ_ONLY)
    private String externalId;

    @NotNull(message = "Paciente {err.mandatory}")
    @Schema(description = "ID del paciente", example = "5001")
    private Long patientId;

    @Schema(description = "Nombre completo del paciente (Solo lectura)", example = "JUAN CARLOS PEREZ", accessMode = Schema.AccessMode.READ_ONLY)
    private String patientFullName;

    @Schema(description = "ID del médico responsable", example = "2001")
    private Long doctorId;

    @Schema(description = "Nombre del médico (Solo lectura)", example = "DR. RODRIGUEZ", accessMode = Schema.AccessMode.READ_ONLY)
    private String doctorFullName;

    @NotNull(message = "Expediente clínico {err.mandatory}")
    @Schema(description = "ID del expediente clínico de contexto", example = "9001")
    private Long medicalHistoryId;

    @Schema(description = "ID del tipo de consulta del catálogo", example = "10")
    private Long typeId;

    @Schema(description = "Nombre del tipo de consulta (Solo lectura)", example = "CONSULTA EXTERNA", accessMode = Schema.AccessMode.READ_ONLY)
    private String typeName;

    @NotNull(message = "Fecha de consulta {err.mandatory}")
    @Schema(description = "Fecha y hora de la consulta", example = "2026-03-25T14:30:00Z")
    private OffsetDateTime consultationDate;

    @Schema(description = "Estado del flujo de consulta", example = "SCHEDULED")
    private ConsultationStatus status;

    @Schema(description = "Nivel de triage o urgencia", example = "NORMAL")
    private String triageLevel;

    @Schema(description = "Notas clínicas y hallazgos")
    private String notes;
}
