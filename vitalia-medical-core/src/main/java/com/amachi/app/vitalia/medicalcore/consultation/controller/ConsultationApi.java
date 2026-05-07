package com.amachi.app.vitalia.medicalcore.consultation.controller;

import com.amachi.app.core.common.controller.GenericApi;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.consultation.dto.ConsultationDto;
import com.amachi.app.vitalia.medicalcore.consultation.dto.search.ConsultationSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.amachi.app.core.common.controller.BaseController.*;

@Tag(name = "Consultation", description = "Registro de consultas médicas (SaaS Elite Tier)")
public interface ConsultationApi extends GenericApi<ConsultationDto> {
    String NAME_API = "Consultation";

    @GetMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get " + NAME_API + " by ID", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    ResponseEntity<ConsultationDto> getConsultationById(@PathVariable("id") @NonNull Long id);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create " + NAME_API, responses = {@ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "400")})
    ResponseEntity<ConsultationDto> createConsultation(@Valid @RequestBody @NonNull ConsultationDto dto);

    @PutMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update " + NAME_API, responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    ResponseEntity<ConsultationDto> updateConsultation(@PathVariable("id") @NonNull Long id, @Valid @RequestBody @NonNull ConsultationDto dto);

    @DeleteMapping(value = ID)
    @Operation(summary = "Delete " + NAME_API, responses = {@ApiResponse(responseCode = "204"), @ApiResponse(responseCode = "404")})
    ResponseEntity<Void> deleteConsultation(@PathVariable("id") @NonNull Long id);

    @GetMapping(value = ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all " + NAME_API)
    ResponseEntity<List<ConsultationDto>> getAllConsultations();

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get paginated " + NAME_API)
    ResponseEntity<PageResponseDto<ConsultationDto>> getPaginatedConsultations(
            @NonNull ConsultationSearchDto searchDto,
            @Parameter(example = "0") @RequestParam(defaultValue = "0", required = false) Integer pageIndex,
            @Parameter(example = "10") @RequestParam(defaultValue = "10", required = false) Integer pageSize);

    @GetMapping(value = "/patient/{patientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get consultations by patient")
    ResponseEntity<List<ConsultationDto>> getConsultationsByPatient(@PathVariable @NonNull Long patientId);

    @GetMapping(value = "/medical-history/{medicalHistoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get consultations by medical history")
    ResponseEntity<List<ConsultationDto>> getConsultationsByMedicalHistory(@PathVariable @NonNull Long medicalHistoryId);
}
