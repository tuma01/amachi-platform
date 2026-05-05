package com.amachi.app.vitalia.medicalcore.appointment.controller;

import com.amachi.app.core.common.controller.GenericApi;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.common.enums.AppointmentStatus;
import com.amachi.app.vitalia.medicalcore.appointment.dto.AppointmentDto;
import com.amachi.app.vitalia.medicalcore.appointment.dto.search.AppointmentSearchDto;
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
 * Interfaz de contrato para la gestion de agendas y citas medicas.
 */
@Tag(name = "Operations - Medical Appointments", description = "REST API for scheduling workflow: Scheduling, Confirmation, and Clinical Attendance.")
public interface AppointmentApi extends GenericApi<AppointmentDto> {
    String NAME_API = "Appointment";

    @Operation(
            summary = "Fetch Appointment Details by ID",
            description = "Retrieves scheduled information, including patient, doctor, specialty, and reason for consultation.",
            responses = {
                    @ApiResponse(responseCode = "200", description = NAME_API + " retrieved successfully."),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied."),
                    @ApiResponse(responseCode = "404", description = NAME_API + " not found."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            }
    )
    @GetMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AppointmentDto> getAppointmentById(
            @Parameter(description = "ID of the " + NAME_API + " to retrieve", required = true)
            @PathVariable("id") @NonNull Long id
    );

    @Operation(
            summary = "Create a " + NAME_API,
            description = "Creates a new " + NAME_API + " using the provided data in the request body.",
            responses = {
                    @ApiResponse(responseCode = "201", description = NAME_API + " created successfully."),
                    @ApiResponse(responseCode = "400", description = "Invalid input data."),
                    @ApiResponse(responseCode = "500", description = "Server error.")
            }
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AppointmentDto> createAppointment(
            @Parameter(description = "Details of the " + NAME_API + " to create.", required = true)
            @Valid @RequestBody @NonNull AppointmentDto dto
    );

    @Operation(
            summary = "Update a " + NAME_API + " by ID",
            description = "Updates an existing " + NAME_API + " using its ID and provided data.",
            responses = {
                    @ApiResponse(responseCode = "200", description = NAME_API + " updated successfully."),
                    @ApiResponse(responseCode = "400", description = "Invalid request: Null ID or incomplete data."),
                    @ApiResponse(responseCode = "404", description = NAME_API + " not found."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            }
    )
    @PutMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AppointmentDto> updateAppointment(
            @Parameter(description = "ID of the " + NAME_API + " to update.", required = true)
            @PathVariable("id") @NonNull Long id,
            @Parameter(description = "New details of the " + NAME_API + ".", required = true)
            @Valid @RequestBody @NonNull AppointmentDto dto
    );

    @Operation(
            summary = "Delete a " + NAME_API + " by ID",
            description = "Deletes an existing " + NAME_API + " using its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = NAME_API + " deleted successfully (no content)."),
                    @ApiResponse(responseCode = "400", description = "Invalid request: Null or invalid ID."),
                    @ApiResponse(responseCode = "404", description = NAME_API + " not found."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            }
    )
    @DeleteMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> deleteAppointment(
            @Parameter(description = "ID of the " + NAME_API + " to delete.", required = true)
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
    ResponseEntity<List<AppointmentDto>> getAllAppointments();

    @Operation(
            summary = "Get a paginated list of " + NAME_API + "s",
            description = "Returns a paginated list of " + NAME_API + "s",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of " + NAME_API + "s retrieved successfully."),
                    @ApiResponse(responseCode = "400", description = "Invalid pagination parameters."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PageResponseDto<AppointmentDto>> getPaginatedAppointments(
            @Parameter(description = "Schedule search filters.") @NonNull AppointmentSearchDto searchDto,
            @Parameter(description = "Page index.", example = "0") @RequestParam(value = "pageIndex", defaultValue = "0", required = false) Integer pageIndex,
            @Parameter(description = "Records per block.", example = "10") @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
    );

    @Operation(
            summary = "Update Appointment Workflow Operational Status",
            description = "Changes the appointment status (e.g., CONFIRMED, CANCELLED, NO_SHOW, ARRIVED).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Appointment workflow updated."),
                    @ApiResponse(responseCode = "400", description = "State transition not allowed."),
                    @ApiResponse(responseCode = "404", description = "Appointment not found."),
                    @ApiResponse(responseCode = "500", description = "System error.")
            }
    )
    @PatchMapping(value = ID + "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AppointmentDto> updateAppointmentStatus(
            @Parameter(description = "ID of the appointment.", required = true) @PathVariable("id") @NonNull Long id,
            @Parameter(description = "New appointment operational status.", required = true) @RequestParam("status") @NonNull AppointmentStatus status
    );
}
