package com.amachi.app.vitalia.medicalcore.clinicalhistory.controller;

import com.amachi.app.vitalia.medicalcore.clinicalhistory.dto.ClinicalEventDto;
import com.amachi.app.vitalia.medicalcore.clinicalhistory.dto.ClinicalSummaryDto;
import com.amachi.app.vitalia.medicalcore.common.enums.ClinicalEventType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Clinical Timeline", description = "Línea de tiempo clínica unificada del paciente (360° view)")
public interface ClinicalTimelineApi {

    @GetMapping(value = "/{patientId}/timeline", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Línea de tiempo clínica completa",
        description = "Devuelve todos los eventos clínicos del paciente ordenados cronológicamente (más reciente primero). Opcionalmente filtrado por tipo.",
        responses = { @ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404") }
    )
    ResponseEntity<List<ClinicalEventDto>> getTimeline(
            @PathVariable("patientId") @NonNull Long patientId,
            @Parameter(description = "Filtrar por tipo de evento (opcional)")
            @RequestParam(value = "type", required = false) ClinicalEventType type);

    @GetMapping(value = "/{patientId}/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Resumen ejecutivo del estado clínico actual",
        description = "Devuelve el estado actual del paciente: condiciones activas, prescripciones vigentes, último encuentro y última observación.",
        responses = { @ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404") }
    )
    ResponseEntity<ClinicalSummaryDto> getSummary(@PathVariable("patientId") @NonNull Long patientId);
}
