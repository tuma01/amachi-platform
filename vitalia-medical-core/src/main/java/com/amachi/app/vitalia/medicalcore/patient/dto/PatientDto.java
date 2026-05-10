package com.amachi.app.vitalia.medicalcore.patient.dto;

import com.amachi.app.core.common.enums.PatientStatus;
import com.amachi.app.core.domain.dto.PersonDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

/**
 * Esquema Profesional Elite para la transferencia de información del paciente.
 * Sigue el estándar GOLD de Amachi Platform.
 */
@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Patient", description = "Esquema integral de Identidad y Perfil Clínico HIS")
public class PatientDto {

    @Schema(description = "Identificador único interno (PK)", example = "5001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Identificador Global Interinstitucional (UUID)", example = "PAT-789-UUID", accessMode = Schema.AccessMode.READ_ONLY)
    private String externalId;

    // --- Identidad (PersonDto) ---
    @Schema(description = "Datos de identidad global de la persona")
    private PersonDto person;

    @Schema(description = "Nombre completo del paciente (Solo lectura)", example = "JUAN PEREZ", accessMode = Schema.AccessMode.READ_ONLY)
    private String patientFullName;

    @NotBlank(message = "NHC {err.mandatory}")
    @Size(max = 50, message = "NHC {err.max.length}")
    @Pattern(regexp = "^[A-Za-z0-9\\-]{2,50}$", message = "NHC {err.format}")
    @Schema(description = "Número de Historia Clínica Local", example = "NHC-2001")
    private String nhc;

    @Size(max = 100, message = "Nacionalidad {err.max.length}")
    @Schema(description = "Nacionalidad de origen", example = "BOLIVIANA")
    private String nationality;

    @Size(max = 100, message = "Nivel de instrucción {err.max.length}")
    @Schema(description = "Nivel de instrucción", example = "LICENCIATURA")
    private String educationLevel;

    @Size(max = 200, message = "Ocupación {err.max.length}")
    @Schema(description = "Profesión u ocupación actual", example = "MEDICO CIRUJANO")
    private String occupation;

    // --- Clínico & Operativo ---
    @Size(max = 10, message = "Idioma {err.max.length}")
    @Schema(description = "Idioma preferido para atención", example = "ES")
    private String preferredLanguage;

    @NotNull(message = "Estado {err.mandatory}")
    @Schema(description = "Estado operativo actual en el flujo clínico", example = "ACTIVE")
    private PatientStatus patientStatus;

    @Schema(description = "ID de la cama asignada (Atajo)", example = "101")
    private Long currentBedId;

    @Schema(description = "ID de la habitación asignada (Atajo)", example = "202")
    private Long roomId;

    @Size(max = 5000, message = "Resumen de alergias {err.max.length}")
    @Schema(description = "Resumen de alergias (Legacy Mirror)", example = "MANI, POLEN")
    private String allergySummary;

    @Schema(description = "ID de la hospitalización vigente", example = "8001")
    private Long currentHospitalizationId;

    @Size(max = 2000, message = "Comentarios adicionales {err.max.length}")
    @Schema(description = "Comentarios o alertas administrativas", example = "Paciente requiere silla de ruedas")
    private String additionalRemarks;

    @Schema(description = "Foto del paciente (Base64/Binary)", example = "byte[] data")
    private byte[] photo;

    // --- Contacto de Emergencia ---
    @Size(max = 200, message = "Nombre contacto emergencia {err.max.length}")
    @Schema(description = "Nombre de la persona de contacto en urgencias", example = "MARIA PEREZ")
    private String emergencyContactName;

    @Size(max = 50, message = "Parentesco {err.max.length}")
    @Schema(description = "Parentesco con el contacto de emergencia", example = "MADRE")
    private String emergencyContactRelationship;

    @Size(max = 50, message = "Teléfono contacto emergencia {err.max.length}")
    @Schema(description = "Teléfono del contacto de emergencia", example = "+591 70000000")
    private String emergencyContactPhone;

    @Email(message = "Email contacto emergencia {err.format}")
    @Size(max = 100, message = "Email contacto emergencia {err.max.length}")
    @Schema(description = "Email del contacto de emergencia", example = "m.perez@email.com")
    private String emergencyContactEmail;

    @Size(max = 500, message = "Dirección contacto emergencia {err.max.length}")
    @Schema(description = "Dirección del contacto de emergencia", example = "AV. PRINCIPAL 456")
    private String emergencyContactAddress;

    @Schema(description = "ID del expediente clínico vigente (solo lectura)", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    private Long medicalHistoryId;

    @Schema(description = "Indica si el paciente está activo en el sistema", example = "true")
    private Boolean active;

    // --- Biometría & Datos Clínicos (desde PatientDetails) ---
    @Schema(description = "ID del Grupo Sanguíneo (Catalog)", example = "1")
    private Long bloodTypeId;

    @Schema(description = "Nombre del Grupo Sanguíneo (Solo lectura)", example = "O POSITIVO")
    private String bloodTypeName;

    @Schema(description = "Peso en kilogramos", example = "75.5")
    private BigDecimal weight;

    @Schema(description = "Estatura en metros", example = "1.75")
    private BigDecimal height;

    @Schema(description = "Indicador de discapacidad", example = "false")
    private Boolean hasDisability;

    @Schema(description = "Detalles de la discapacidad", example = "Discapacidad motriz leve")
    private String disabilityDetails;

    @Schema(description = "Estado de embarazo", example = "false")
    private Boolean isPregnant;

    @Min(value = 0, message = "Semanas de embarazo {err.min.value}")
    @Max(value = 45, message = "Semanas de embarazo {err.max.value}")
    @Schema(description = "Semanas de embarazo", example = "12")
    private Integer gestationalWeeks;

    @Min(value = 0, message = "Número de hijos {err.min.value}")
    @Schema(description = "Número de hijos", example = "2")
    private Integer childrenCount;

    @Schema(description = "Grupo étnico de auto-identificación", example = "Hispano")
    private String ethnicGroup;
}
