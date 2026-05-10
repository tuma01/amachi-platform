package com.amachi.app.vitalia.medicalcore.medicationadministration.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.vitalia.medicalcore.common.enums.AdministrationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para administraciones de medicamentos")
public class MedicationAdministrationSearchDto extends BaseSearchDto {

    @Schema(description = "ID del paciente", example = "5001")
    private Long patientId;

    @Schema(description = "ID de la prescripción", example = "6001")
    private Long prescriptionId;

    @Schema(description = "ID de la enfermera", example = "3001")
    private Long nurseId;

    @Schema(description = "Estado de la administración", example = "COMPLETED")
    private AdministrationStatus status;
}
