package com.amachi.app.vitalia.medicalcore.prescription.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.vitalia.medicalcore.common.enums.MedicationRequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para Prescripciones Médicas")
public class PrescriptionSearchDto extends BaseSearchDto {

    @Schema(description = "ID del paciente", example = "5001")
    private Long patientId;

    @Schema(description = "ID del médico prescriptor", example = "2001")
    private Long doctorId;

    @Schema(description = "ID del encuentro clínico", example = "12001")
    private Long encounterId;

    @Schema(description = "Estado de la prescripción", example = "ACTIVE")
    private MedicationRequestStatus status;
}
