package com.amachi.app.vitalia.medicalcore.medicalhistory.controller;

import com.amachi.app.core.common.controller.GenericApi;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.MedicalHistoryDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.search.MedicalHistorySearchDto;
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

@Tag(name = "MedicalHistory", description = "Expediente clínico longitudinal del paciente (EHR)")
public interface MedicalHistoryApi extends GenericApi<MedicalHistoryDto> {
    String NAME_API = "MedicalHistory";

    @GetMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get " + NAME_API + " by ID", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    ResponseEntity<MedicalHistoryDto> getMedicalHistoryById(@PathVariable("id") @NonNull Long id);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create " + NAME_API, responses = {@ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "400")})
    ResponseEntity<MedicalHistoryDto> createMedicalHistory(@Valid @RequestBody @NonNull MedicalHistoryDto dto);

    @PutMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update " + NAME_API, responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    ResponseEntity<MedicalHistoryDto> updateMedicalHistory(@PathVariable("id") @NonNull Long id, @Valid @RequestBody @NonNull MedicalHistoryDto dto);

    @DeleteMapping(value = ID)
    @Operation(summary = "Delete " + NAME_API, responses = {@ApiResponse(responseCode = "204"), @ApiResponse(responseCode = "404")})
    ResponseEntity<Void> deleteMedicalHistory(@PathVariable("id") @NonNull Long id);

    @GetMapping(value = ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all " + NAME_API)
    ResponseEntity<List<MedicalHistoryDto>> getAllMedicalHistories();

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get paginated " + NAME_API)
    ResponseEntity<PageResponseDto<MedicalHistoryDto>> getPaginatedMedicalHistories(
            @NonNull MedicalHistorySearchDto searchDto,
            @Parameter(example = "0") @RequestParam(defaultValue = "0", required = false) Integer pageIndex,
            @Parameter(example = "10") @RequestParam(defaultValue = "10", required = false) Integer pageSize);
}
