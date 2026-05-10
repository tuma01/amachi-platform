package com.amachi.app.vitalia.medicalcore.clinicalhistory.dto;

import com.amachi.app.vitalia.medicalcore.encounter.dto.EncounterDto;
import com.amachi.app.vitalia.medicalcore.encounter.dto.ConditionDto;
import com.amachi.app.vitalia.medicalcore.observation.dto.ObservationDto;
import com.amachi.app.vitalia.medicalcore.prescription.dto.PrescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Schema(description = "Resumen ejecutivo del estado clínico actual del paciente")
public class ClinicalSummaryDto {

    @Schema(description = "ID del paciente", example = "5001")
    private Long patientId;

    @Schema(description = "Nombre completo del paciente", example = "JUAN CARLOS PÉREZ")
    private String patientFullName;

    @Schema(description = "Total de encuentros registrados", example = "12")
    private long totalEncounters;

    @Schema(description = "Total de condiciones activas", example = "3")
    private long activeConditions;

    @Schema(description = "Total de prescripciones activas", example = "2")
    private long activePrescriptions;

    @Schema(description = "Total de hospitalizaciones", example = "1")
    private long totalHospitalizations;

    @Schema(description = "Último encuentro clínico registrado")
    private EncounterDto latestEncounter;

    @Schema(description = "Última observación clínica registrada")
    private ObservationDto latestObservation;

    @Schema(description = "Lista de condiciones activas")
    private List<ConditionDto> activeConditionList;

    @Schema(description = "Lista de prescripciones activas")
    private List<PrescriptionDto> activePrescriptionList;
}
