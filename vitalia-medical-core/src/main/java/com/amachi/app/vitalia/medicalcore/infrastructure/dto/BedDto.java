package com.amachi.app.vitalia.medicalcore.infrastructure.dto;

import com.amachi.app.core.common.enums.BedStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Bed", description = "Cama hospitalaria individual dentro de una habitación")
public class BedDto {

    @Schema(description = "Identificador único de la cama", example = "301")
    private Long id;

    @NotNull(message = "Habitación {err.mandatory}")
    @Schema(description = "ID de la habitación a la que pertenece la cama", example = "202")
    private Long roomId;

    @Schema(description = "Número visible de la habitación (solo lectura)", example = "301-A", accessMode = Schema.AccessMode.READ_ONLY)
    private String roomNumber;

    @NotBlank(message = "Número de cama {err.mandatory}")
    @Schema(description = "Número rotulado físicamente en la cama", example = "01")
    private String bedNumber;

    @NotBlank(message = "Código de cama {err.mandatory}")
    @Schema(description = "Código único interno de la cama por habitación", example = "UCI-N3-301A-01")
    private String bedCode;

    @Schema(description = "Indica si la cama está ocupada por un paciente", example = "false")
    private Boolean isOccupied;

    @Schema(description = "Estado operativo de la cama", example = "AVAILABLE")
    private BedStatusEnum status;

    @Schema(description = "Descripción o notas adicionales sobre la cama")
    private String description;

    @Schema(description = "Fecha de mantenimiento programado", example = "2026-06-15")
    private LocalDate maintenanceDue;

    @Schema(description = "Indica si la cama está activa y disponible para asignación", example = "true")
    private Boolean active;
}
