package com.amachi.app.vitalia.medicalcore.medicationadministration.controller;

import com.amachi.app.core.common.controller.GenericApi;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.medicationadministration.dto.MedicationAdministrationDto;
import com.amachi.app.vitalia.medicalcore.medicationadministration.dto.search.MedicationAdministrationSearchDto;
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

@Tag(name = "MedicationAdministration", description = "Administración de medicamentos al paciente (FHIR MedicationAdministration)")
public interface MedicationAdministrationApi extends GenericApi<MedicationAdministrationDto> {

    String NAME_API = "MedicationAdministration";

    @GetMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get " + NAME_API + " by ID",
               responses = { @ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404") })
    ResponseEntity<MedicationAdministrationDto> getMedicationAdministrationById(@PathVariable("id") @NonNull Long id);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create " + NAME_API,
               responses = { @ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "400") })
    ResponseEntity<MedicationAdministrationDto> createMedicationAdministration(@Valid @RequestBody @NonNull MedicationAdministrationDto dto);

    @PutMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update " + NAME_API,
               responses = { @ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404") })
    ResponseEntity<MedicationAdministrationDto> updateMedicationAdministration(@PathVariable("id") @NonNull Long id,
                                                                               @Valid @RequestBody @NonNull MedicationAdministrationDto dto);

    @DeleteMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete " + NAME_API,
               responses = { @ApiResponse(responseCode = "204"), @ApiResponse(responseCode = "404") })
    ResponseEntity<Void> deleteMedicationAdministration(@PathVariable("id") @NonNull Long id);

    @GetMapping(value = ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all " + NAME_API)
    ResponseEntity<List<MedicationAdministrationDto>> getAllMedicationAdministrations();

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get paginated " + NAME_API)
    ResponseEntity<PageResponseDto<MedicationAdministrationDto>> getPaginatedMedicationAdministrations(
            @NonNull MedicationAdministrationSearchDto searchDto,
            @Parameter(example = "0") @RequestParam(value = "pageIndex", defaultValue = "0", required = false) Integer pageIndex,
            @Parameter(example = "10") @RequestParam(value = "pageSize",  defaultValue = "10", required = false) Integer pageSize);

    @GetMapping(value = "/prescription/{prescriptionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get administrations by Prescription")
    ResponseEntity<List<MedicationAdministrationDto>> getByPrescription(@PathVariable("prescriptionId") @NonNull Long prescriptionId);
}
