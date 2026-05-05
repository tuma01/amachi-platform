package com.amachi.app.vitalia.medicalcore.doctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

/**
 * Esquema Profesional Elite para el personal médico facultativo.
 * Estándar GOLD de Amachi Platform.
 */
@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Doctor", description = "Perfil integral de Facultativo (Identidad + Clínica + Nómina)")
public class DoctorDto {

    @Schema(description = "Identificador único interno (PK)", example = "2001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Identificador Global del Facultativo (UUID)", example = "DOC-7788-UUID", accessMode = Schema.AccessMode.READ_ONLY)
    private String externalId;

    // --- Identidad (PersonDto) ---
    @Schema(description = "Datos de identidad global de la persona")
    private com.amachi.app.core.domain.dto.PersonDto person;

    @Schema(description = "Nombre completo del facultativo (Solo lectura)", example = "Dr. Armando Casas", accessMode = Schema.AccessMode.READ_ONLY)
    private String doctorFullName;

    // --- Credenciales y Facturación (Billing) ---
    @NotBlank(message = "Licencia {err.mandatory}")
    @Schema(description = "Número de matrícula profesional habilitante", example = "MED-BOL-789-V")
    private String licenseNumber;

    @Schema(description = "Número de proveedor (RAMQ/Billing)", example = "123456")
    private String providerNumber;

    @Schema(description = "Especialidad médica principal (Resumen)", example = "CARDIOLOGÍA INFANTIL")
    private String specialtiesSummary;

    @Schema(description = "IDs de las especialidades vinculadas del catálogo global", example = "[1, 2, 5]")
    private Set<Long> specialtyIds;

    @Schema(description = "Trayectoria resumida (Bio)", example = "15 años en cirugía cardiovascular.")
    private String bio;

    // --- Operativo & Nómina ---
    @Schema(description = "ID de la Unidad Departamental de adscripción", example = "101")
    private Long departmentUnitId;

    @Schema(description = "Nombre de la Unidad Departamental (Solo lectura)", example = "CARDIOLOGÍA - ALA NORTE", accessMode = Schema.AccessMode.READ_ONLY)
    private String departmentUnitName;

    @Schema(description = "ID de la ficha de relación laboral (RRHH)", example = "5001")
    private Long employeeId;

    @Schema(description = "Precio base de la consulta externa", example = "50.00")
    private BigDecimal consultationPrice;

    @Schema(description = "Estado de disponibilidad actual", example = "AVAILABLE")
    private String availabilityStatus;

    @Schema(description = "Ruta digital de la firma para documentos electrónicos", example = "/signatures/dr_rodriguez.png")
    private String signatureDigitalPath;

    @Schema(description = "Fecha de expiración de la licencia médica", example = "2030-12-31")
    private LocalDate licenseExpiryDate;

    @Schema(description = "Fecha formal de contratación", example = "2020-01-15")
    private LocalDate hireDate;

    @Schema(description = "Extensión o box de consultorio", example = "BOX-01")
    private String officeNumber;

    // --- Métricas & Habilidades ---
    @Schema(description = "Años de experiencia clínica", example = "12")
    private Integer yearsOfExperience;

    @Schema(description = "Total de consultas atendidas históricamente", example = "1540", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer totalConsultations;

    @Schema(description = "Rating promedio de satisfacción del paciente (1-5)", example = "4.8", accessMode = Schema.AccessMode.READ_ONLY)
    private Double rating;

    @Schema(description = "Catálogo de procedimientos quirúrgicos autorizados")
    private Set<String> clinicalProcedures;

    @Schema(description = "Indica si el facultativo está activo", example = "true")
    private Boolean active;
}
