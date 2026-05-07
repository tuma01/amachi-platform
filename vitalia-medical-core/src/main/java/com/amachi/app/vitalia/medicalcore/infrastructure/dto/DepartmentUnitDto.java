package com.amachi.app.vitalia.medicalcore.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "DepartmentUnit", description = "Unidad funcional hospitalaria (departamento, piso, servicio clínico)")
public class DepartmentUnitDto {

    @Schema(description = "Identificador único de la unidad", example = "101")
    private Long id;

    @NotBlank(message = "Nombre de unidad {err.mandatory}")
    @Size(max = 150)
    @Schema(description = "Nombre descriptivo de la unidad", example = "UCI — Ala Norte Piso 3")
    private String name;

    @NotBlank(message = "Código de unidad {err.mandatory}")
    @Size(max = 30)
    @Schema(description = "Código funcional único por tenant", example = "UCI-N3")
    private String code;

    @Schema(description = "Piso o nivel físico", example = "3")
    private String floor;

    @Schema(description = "Teléfono de contacto de la unidad", example = "+591-2-2123456")
    private String contactPhone;

    @Schema(description = "Descripción o notas de la unidad")
    private String description;

    @Schema(description = "Indica si la unidad está activa y operativa", example = "true")
    private Boolean active;

    @Schema(description = "Capacidad máxima de pacientes", example = "20")
    private Integer maxCapacity;

    @Schema(description = "Indica si es una unidad clínica (false = administrativa)", example = "true")
    private Boolean isClinical;

    @Schema(description = "Centro de costo contable", example = "CC-003")
    private String costCenter;

    @Schema(description = "ID de la unidad padre en jerarquía hospitalaria", example = "50")
    private Long parentUnitId;

    @Schema(description = "Nombre de la unidad padre (solo lectura)", example = "PISO 3 — ALA NORTE", accessMode = Schema.AccessMode.READ_ONLY)
    private String parentUnitName;

    @Schema(description = "ID del jefe de unidad (Empleado)", example = "5001")
    private Long unitHeadId;

    @Schema(description = "Nombre del jefe de unidad (solo lectura)", example = "LIC. CARLOS MENDEZ", accessMode = Schema.AccessMode.READ_ONLY)
    private String unitHeadName;
}
