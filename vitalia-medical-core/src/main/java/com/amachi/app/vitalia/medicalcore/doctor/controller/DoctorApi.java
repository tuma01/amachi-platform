package com.amachi.app.vitalia.medicalcore.doctor.controller;

import com.amachi.app.core.common.controller.GenericApi;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.geography.country.dto.CountryDto;
import com.amachi.app.vitalia.medicalcore.doctor.dto.DoctorDto;
import com.amachi.app.vitalia.medicalcore.doctor.dto.search.DoctorSearchDto;
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
 * Interfaz de contrato para la gestion del personal medico facultativo.
 */
@Tag(name = "Staff - Doctors", description = "Comprehensive management of medical professional profiles, specialties, and professional accreditations.")
public interface DoctorApi extends GenericApi<DoctorDto> {
    String NAME_API = "Doctor";

    @Operation(
            summary = "Fetch Doctor by ID",
            description = "Retrieves the detailed professional profile of the practitioner, including global identity, specialties, and operational data.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Doctor profile retrieved successfully."),
                    @ApiResponse(responseCode = "400", description = "Invalid or malformed ID."),
                    @ApiResponse(responseCode = "404", description = "Doctor does not exist or does not belong to the hospital."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            }
    )
    @GetMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DoctorDto> getDoctorById(
            @Parameter(description = "Internal practitioner identifier", required = true, example = "2001")
            @PathVariable("id") @NonNull Long id
    );

    @Operation(
            summary = "Register New Practitioner",
            description = "Creates a master record for medical personnel. If the person does not exist, their global identity is created. The doctor is automatically linked to the hospital (Tenant).",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Doctor registered and accredited successfully."),
                    @ApiResponse(responseCode = "400", description = "Validation error in practitioner data."),
                    @ApiResponse(responseCode = "409", description = "Conflict: Doctor already registered with that license or identity."),
                    @ApiResponse(responseCode = "500", description = "Error persisting the professional profile.")
            }
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DoctorDto> createDoctor(
            @Parameter(description = "Full doctor DTO to be registered.", required = true)
            @Valid @NonNull @RequestBody DoctorDto dto
    );

    @Operation(
            summary = "Update Professional Profile",
            description = "Allows modifying the doctor's operational information, such as consultation price, availability, or professional bio.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile updated successfully."),
                    @ApiResponse(responseCode = "400", description = "Invalid update data."),
                    @ApiResponse(responseCode = "404", description = "Doctor not found."),
                    @ApiResponse(responseCode = "500", description = "Critical failure during update.")
            }
    )
    @PutMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DoctorDto> updateDoctor(
            @Parameter(description = "ID of the doctor to be modified.", required = true)
            @PathVariable("id") @NonNull Long id,
            @Parameter(description = "DTO with updated fields.", required = true)
            @Valid @NonNull @RequestBody DoctorDto dto
    );

    @Operation(
            summary = "Practitioner Termination (Soft-Delete)",
            description = "Applies a logical deletion to the doctor's record, preserving the integrity of historical clinical records.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Doctor successfully terminated."),
                    @ApiResponse(responseCode = "400", description = "Cannot terminate (active patients or pending surgeries)."),
                    @ApiResponse(responseCode = "404", description = "Doctor not found."),
                    @ApiResponse(responseCode = "500", description = "Error processing termination.")
            }
    )
    @DeleteMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> deleteDoctor(
            @Parameter(description = "ID of the doctor to be deleted.", required = true)
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
    ResponseEntity<List<DoctorDto>> getAllDoctors();

    @Operation(
            summary = "Expert Practitioner Search",
            description = "Advanced search with support for filters by name, specialty, license, and departmental unit. Paginated results.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Results page retrieved successfully."),
                    @ApiResponse(responseCode = "500", description = "Failure in expert search execution.")
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PageResponseDto<DoctorDto>> getPaginatedDoctors(
            @NonNull DoctorSearchDto searchDto,
            @RequestParam(value = "pageIndex", defaultValue = "0", required = false) Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
    );
}
