package com.amachi.app.vitalia.medicalcore.medicationdispense.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.vitalia.medicalcore.common.enums.DispenseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para dispensaciones de medicamentos")
public class MedicationDispenseSearchDto extends BaseSearchDto {

    @Schema(description = "ID del paciente", example = "5001")
    private Long patientId;

    @Schema(description = "ID de la prescripción", example = "6001")
    private Long prescriptionId;

    @Schema(description = "ID del dispensador", example = "2001")
    private Long dispenserId;

    @Schema(description = "Estado de la dispensación", example = "COMPLETED")
    private DispenseStatus status;
}
