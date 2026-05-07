package com.amachi.app.vitalia.medicalcore.hospitalization.controller;

import com.amachi.app.core.common.controller.GenericApi;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.DischargeMedicationDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.search.DischargeMedicationSearchDto;
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

@Tag(name = "DischargeMedication", description = "Medicación prescrita en el alta hospitalaria")
public interface DischargeMedicationApi extends GenericApi<DischargeMedicationDto> {
    String NAME_API = "DischargeMedication";

    @GetMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get " + NAME_API + " by ID", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    ResponseEntity<DischargeMedicationDto> getDischargeMedicationById(@PathVariable("id") @NonNull Long id);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create " + NAME_API, responses = {@ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "400")})
    ResponseEntity<DischargeMedicationDto> createDischargeMedication(@Valid @RequestBody @NonNull DischargeMedicationDto dto);

    @PutMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update " + NAME_API, responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    ResponseEntity<DischargeMedicationDto> updateDischargeMedication(@PathVariable("id") @NonNull Long id, @Valid @RequestBody @NonNull DischargeMedicationDto dto);

    @DeleteMapping(value = ID)
    @Operation(summary = "Delete " + NAME_API, responses = {@ApiResponse(responseCode = "204"), @ApiResponse(responseCode = "404")})
    ResponseEntity<Void> deleteDischargeMedication(@PathVariable("id") @NonNull Long id);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get paginated " + NAME_API)
    ResponseEntity<PageResponseDto<DischargeMedicationDto>> getPaginatedDischargeMedications(
            @NonNull DischargeMedicationSearchDto searchDto,
            @Parameter(example = "0") @RequestParam(defaultValue = "0", required = false) Integer pageIndex,
            @Parameter(example = "10") @RequestParam(defaultValue = "10", required = false) Integer pageSize);

    @GetMapping(value = "/hospitalization/{hospitalizationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all discharge medications for a hospitalization")
    ResponseEntity<List<DischargeMedicationDto>> getByHospitalization(@PathVariable @NonNull Long hospitalizationId);
}
