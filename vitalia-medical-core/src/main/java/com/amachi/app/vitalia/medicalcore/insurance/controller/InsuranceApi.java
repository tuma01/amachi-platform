package com.amachi.app.vitalia.medicalcore.insurance.controller;

import com.amachi.app.core.common.controller.GenericApi;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.insurance.dto.InsuranceDto;
import com.amachi.app.vitalia.medicalcore.insurance.dto.search.InsuranceSearchDto;
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

@Tag(name = "Insurance", description = "Gestión de seguros médicos del paciente")
public interface InsuranceApi extends GenericApi<InsuranceDto> {
    String NAME_API = "Insurance";

    @GetMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get " + NAME_API + " by ID", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    ResponseEntity<InsuranceDto> getInsuranceById(@PathVariable("id") @NonNull Long id);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create " + NAME_API, responses = {@ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "400")})
    ResponseEntity<InsuranceDto> createInsurance(@Valid @RequestBody @NonNull InsuranceDto dto);

    @PutMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update " + NAME_API, responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    ResponseEntity<InsuranceDto> updateInsurance(@PathVariable("id") @NonNull Long id, @Valid @RequestBody @NonNull InsuranceDto dto);

    @DeleteMapping(value = ID)
    @Operation(summary = "Delete " + NAME_API, responses = {@ApiResponse(responseCode = "204"), @ApiResponse(responseCode = "404")})
    ResponseEntity<Void> deleteInsurance(@PathVariable("id") @NonNull Long id);

    @GetMapping(value = ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all " + NAME_API)
    ResponseEntity<List<InsuranceDto>> getAllInsurances();

    @GetMapping(value = "/by-patient/{patientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all insurances for a specific patient")
    ResponseEntity<List<InsuranceDto>> getInsurancesByPatient(@PathVariable("patientId") @NonNull Long patientId);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get paginated " + NAME_API)
    ResponseEntity<PageResponseDto<InsuranceDto>> getPaginatedInsurances(
            @NonNull InsuranceSearchDto searchDto,
            @Parameter(example = "0") @RequestParam(defaultValue = "0", required = false) Integer pageIndex,
            @Parameter(example = "10") @RequestParam(defaultValue = "10", required = false) Integer pageSize);
}
