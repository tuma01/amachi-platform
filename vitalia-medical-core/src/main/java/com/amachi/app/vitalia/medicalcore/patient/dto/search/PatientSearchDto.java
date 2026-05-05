package com.amachi.app.vitalia.medicalcore.patient.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.core.common.enums.PatientStatus;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Filtros de búsqueda dinámica ÉLITE para Pacientes (Standard GOLD HIS).
 */
@Getter @Setter @SuperBuilder
@NoArgsConstructor @AllArgsConstructor
@Hidden
public class PatientSearchDto extends BaseSearchDto {

    @Schema(description = "Búsqueda textual (Nombre, Apellidos o NHC)")
    private String query;

    @Schema(description = "Identificador único global (UUID)")
    private String externalId;

    @Schema(description = "Documento de Identidad Nacional")
    private String nationalId;

    @Schema(description = "Número de Historia Clínica")
    private String nhc;

    @Schema(description = "Correo electrónico")
    private String email;

    @Schema(description = "Número de teléfono móvil")
    private String mobileNumber;

    @Schema(description = "Estado operativo del paciente")
    private PatientStatus patientStatus;

    @Schema(description = "ID de la cama asignada")
    private Long currentBedId;
}
