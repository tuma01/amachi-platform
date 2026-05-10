package com.amachi.app.vitalia.medicalcore.medicationdispense.controller;

import com.amachi.app.core.common.controller.GenericApi;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.medicationdispense.dto.MedicationDispenseDto;
import com.amachi.app.vitalia.medicalcore.medicationdispense.dto.search.MedicationDispenseSearchDto;
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

@Tag(name = "MedicationDispense", description = "Dispensación de medicamentos (FHIR MedicationDispense)")
public interface MedicationDispenseApi extends GenericApi<MedicationDispenseDto> {

    String NAME_API = "MedicationDispense";

    @GetMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get " + NAME_API + " by ID",
               responses = { @ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404") })
    ResponseEntity<MedicationDispenseDto> getMedicationDispenseById(@PathVariable("id") @NonNull Long id);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create " + NAME_API,
               responses = { @ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "400") })
    ResponseEntity<MedicationDispenseDto> createMedicationDispense(@Valid @RequestBody @NonNull MedicationDispenseDto dto);

    @PutMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update " + NAME_API,
               responses = { @ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404") })
    ResponseEntity<MedicationDispenseDto> updateMedicationDispense(@PathVariable("id") @NonNull Long id,
                                                                   @Valid @RequestBody @NonNull MedicationDispenseDto dto);

    @DeleteMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete " + NAME_API,
               responses = { @ApiResponse(responseCode = "204"), @ApiResponse(responseCode = "404") })
    ResponseEntity<Void> deleteMedicationDispense(@PathVariable("id") @NonNull Long id);

    @GetMapping(value = ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all " + NAME_API)
    ResponseEntity<List<MedicationDispenseDto>> getAllMedicationDispenses();

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get paginated " + NAME_API)
    ResponseEntity<PageResponseDto<MedicationDispenseDto>> getPaginatedMedicationDispenses(
            @NonNull MedicationDispenseSearchDto searchDto,
            @Parameter(example = "0") @RequestParam(value = "pageIndex", defaultValue = "0", required = false) Integer pageIndex,
            @Parameter(example = "10") @RequestParam(value = "pageSize",  defaultValue = "10", required = false) Integer pageSize);

    @GetMapping(value = "/prescription/{prescriptionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get dispenses by Prescription")
    ResponseEntity<List<MedicationDispenseDto>> getByPrescription(@PathVariable("prescriptionId") @NonNull Long prescriptionId);
}
