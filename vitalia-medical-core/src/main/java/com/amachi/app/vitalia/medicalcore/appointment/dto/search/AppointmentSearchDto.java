package com.amachi.app.vitalia.medicalcore.appointment.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.core.common.enums.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * Search DTO for Appointments (SaaS Elite Tier).
 * Self-documented for OpenAPI standard.
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda avanzada para Agendas Médicas")
public class AppointmentSearchDto extends BaseSearchDto {

    @Schema(description = "ID del Paciente", example = "1")
    private Long patientId;

    @Schema(description = "ID del Médico", example = "5")
    private Long doctorId;

    @Schema(description = "ID del Consultorio/Habitación", example = "10")
    private Long roomId;

    @Schema(description = "Estado de la cita")
    private AppointmentStatus status;

    @Schema(description = "Origen de la cita (WEB, APP, CALL_CENTER)", example = "WEB")
    private String source;

    @Schema(description = "Filtro para inasistencias")
    private Boolean noShow;

    @Schema(description = "Fecha inicio desde")
    private OffsetDateTime startFrom;

    @Schema(description = "Fecha inicio hasta")
    private OffsetDateTime startTo;
}
