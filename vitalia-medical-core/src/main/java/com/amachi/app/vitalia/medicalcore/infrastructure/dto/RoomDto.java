package com.amachi.app.vitalia.medicalcore.infrastructure.dto;

import com.amachi.app.core.common.enums.RoomTypeEnum;
import com.amachi.app.vitalia.medicalcore.common.enums.CleaningStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Room", description = "Habitación, consultorio o box físico dentro de una unidad hospitalaria")
public class RoomDto {

    @Schema(description = "Identificador único de la habitación", example = "202")
    private Long id;

    @NotNull(message = "Unidad hospitalaria {err.mandatory}")
    @Schema(description = "ID de la unidad a la que pertenece la habitación", example = "101")
    private Long unitId;

    @Schema(description = "Nombre de la unidad (solo lectura)", example = "UCI — ALA NORTE", accessMode = Schema.AccessMode.READ_ONLY)
    private String unitName;

    @NotBlank(message = "Número de habitación {err.mandatory}")
    @Schema(description = "Identificador físico visible de la habitación", example = "301-A")
    private String roomNumber;

    @Schema(description = "Indica si es habitación privada (individual)", example = "false")
    private Boolean privateRoom;

    @Schema(description = "Categoría o tipo de habitación")
    private RoomTypeEnum roomType;

    @Schema(description = "Número de piso o nivel físico", example = "3")
    private Integer blockFloor;

    @Schema(description = "Código del ala o bloque hospitalario", example = "ALA-NORTE")
    private String blockCode;

    @Schema(description = "Descripción adicional de la habitación")
    private String description;

    @Schema(description = "Estado de limpieza actual", example = "CLEAN")
    private CleaningStatus cleaningStatus;

    @Schema(description = "Indica si la habitación está operativa", example = "true")
    private Boolean active;

    @Schema(description = "Número de camas disponibles (solo lectura)", example = "4", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer bedCount;
}
