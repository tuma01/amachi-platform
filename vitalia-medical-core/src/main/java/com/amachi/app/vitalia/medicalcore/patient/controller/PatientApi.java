package com.amachi.app.vitalia.medicalcore.patient.controller;

import com.amachi.app.core.common.controller.GenericApi;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.geography.country.dto.CountryDto;
import com.amachi.app.vitalia.medicalcore.patient.dto.PatientDto;
import com.amachi.app.vitalia.medicalcore.patient.dto.search.PatientSearchDto;
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

import static com.amachi.app.core.common.controller.BaseController.ALL;
import static com.amachi.app.core.common.controller.BaseController.ID;

/**
 * Interfaz de contrato para la gestion del expediente maestro del paciente.
 * Sigue el patron profesional y homogeneo de Vitalia.
 */
@Tag(name = "Patient Management", description = "REST API for patient lifecycle: Registration, Inpatient Care, and Follow-up.")
public interface PatientApi extends GenericApi<PatientDto> {
    String NAME_API = "Patient";

    @Operation(
            summary = "Fetch Master Patient Record by ID",
            description = "Returns the clinical profile and global identity of the patient. Requires belonging to the same Tenant.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Record located successfully."),
                    @ApiResponse(responseCode = "404", description = "Patient not found in the hospital."),
                    @ApiResponse(responseCode = "500", description = "Internal system error.")
            }
    )
    @GetMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PatientDto> getPatientById(
            @Parameter(description = "Internal patient ID", required = true, example = "5001")
            @PathVariable("id") @NonNull Long id
    );

    @Operation(
            summary = "Register New Patient Admission",
            description = "Creates a new clinical record linked to a Global Identity (Person).",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Patient registered and linked successfully."),
                    @ApiResponse(responseCode = "400", description = "Conflict with NHC or invalid data."),
                    @ApiResponse(responseCode = "500", description = "Error persisting the record.")
            }
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PatientDto> createPatient(
            @Parameter(description = "Full patient schema", required = true)
            @Valid @NonNull @RequestBody PatientDto dto
    );

    @Operation(
            summary = "Update Patient Clinical Profile",
            description = "Allows modifying demographic, biometric, and local contact data.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile updated successfully."),
                    @ApiResponse(responseCode = "404", description = "Patient not found."),
                    @ApiResponse(responseCode = "500", description = "Data integrity error.")
            }
    )
    @PutMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PatientDto> updatePatient(
            @Parameter(description = "ID of the patient to be modified", required = true)
            @PathVariable("id") @NonNull Long id,
            @Parameter(description = "Updated data", required = true)
            @Valid @NonNull @RequestBody PatientDto dto
    );

    @Operation(
            summary = "Logical Deletion of Clinical Record (Soft-Delete)",
            description = "Applies Soft-Delete to the patient in the current hospital. The global identity remains intact.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Record archived successfully (Soft-Delete)."),
                    @ApiResponse(responseCode = "404", description = "Patient not found.")
            }
    )
    @DeleteMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> deletePatient(
            @Parameter(description = "ID of the patient to be deleted", required = true)
            @PathVariable("id") @NonNull Long id
    );

    @Operation(
            summary = "Get all " + NAME_API + "s",
            description = "Returns the complete list of " + NAME_API + "s",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of " + NAME_API + "s retrieved successfully."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            }
    )
    @GetMapping(value = ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<PatientDto>> getAllPatients();

    @Operation(
            summary = "Advanced Patient Search",
            description = "Paginated query with filters for NHC, Name, National ID, and Status. Automatic multi-tenant isolation.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully.")
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PageResponseDto<PatientDto>> getPaginatedPatients(
            @Parameter(description = "Dynamic search filters") @NonNull PatientSearchDto searchDto,
            @Parameter(description = "Page index", example = "0") @RequestParam(value = "pageIndex", defaultValue = "0", required = false) Integer pageIndex,
            @Parameter(description = "Records per page", example = "10") @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
    );
}
