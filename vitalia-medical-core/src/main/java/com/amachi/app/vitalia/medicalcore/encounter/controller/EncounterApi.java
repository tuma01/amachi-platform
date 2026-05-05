package com.amachi.app.vitalia.medicalcore.encounter.controller;

import com.amachi.app.core.common.controller.GenericApi;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.encounter.dto.ConditionDto;
import com.amachi.app.vitalia.medicalcore.encounter.dto.EncounterDto;
import com.amachi.app.vitalia.medicalcore.encounter.dto.request.ConditionRequest;
import com.amachi.app.vitalia.medicalcore.encounter.dto.request.StartEncounterRequest;
import com.amachi.app.vitalia.medicalcore.encounter.dto.search.EncounterSearchDto;
import com.amachi.app.vitalia.medicalcore.prescription.entity.Prescription;
import com.amachi.app.vitalia.medicalcore.observation.dto.ObservationDto;
import com.amachi.app.vitalia.medicalcore.observation.dto.request.ObservationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.amachi.app.core.common.controller.BaseController.ALL;
import static com.amachi.app.core.common.controller.BaseController.ID;

@Tag(name = "Encounter", description = "REST API for managing clinical encounters (FHIR Encounter). Orchestrates clinical workflows.")
public interface EncounterApi extends GenericApi<EncounterDto> {
    String NAME_API = "Encounter";

    @Operation(
            summary = "Get " + NAME_API + " by ID",
            description = "Returns a specific " + NAME_API + " by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = NAME_API + " retrieved successfully."),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied."),
                    @ApiResponse(responseCode = "404", description = NAME_API + " not found."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            }
    )
    @GetMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EncounterDto> getEncounterById(
            @Parameter(description = "ID of the encounter", required = true)
            @PathVariable("id") @NonNull Long id
    );

    @Operation(
            summary = "Create " + NAME_API,
            description = "Creates a new encounter.",
            responses = {
                    @ApiResponse(responseCode = "201", description = NAME_API + " created successfully."),
                    @ApiResponse(responseCode = "400", description = "Invalid input data."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            }
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EncounterDto> createEncounter(
            @Parameter(description = "Encounter payload", required = true)
            @Valid @RequestBody EncounterDto dto
    );

    @Operation(
            summary = "Update " + NAME_API,
            description = "Updates an existing encounter.",
            responses = {
                    @ApiResponse(responseCode = "200", description = NAME_API + " updated successfully."),
                    @ApiResponse(responseCode = "400", description = "Invalid input."),
                    @ApiResponse(responseCode = "404", description = NAME_API + " not found."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            }
    )
    @PutMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EncounterDto> updateEncounter(
            @Parameter(description = "Encounter ID", required = true)
            @PathVariable("id") Long id,
            @Valid @RequestBody EncounterDto dto
    );

    @Operation(
            summary = "Delete " + NAME_API,
            description = "Deletes an encounter by ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = NAME_API + " deleted successfully."),
                    @ApiResponse(responseCode = "400", description = "Invalid ID."),
                    @ApiResponse(responseCode = "404", description = NAME_API + " not found."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            }
    )
    @DeleteMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> deleteEncounter(@PathVariable("id") Long id);

    @Operation(
            summary = "Get all encounters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Encounters retrieved successfully."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            }
    )
    @GetMapping(value = ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<EncounterDto>> getAllEncounters();

    @Operation(
            summary = "Search encounters (paginated)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Encounters retrieved successfully."),
                    @ApiResponse(responseCode = "400", description = "Invalid pagination parameters."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PageResponseDto<EncounterDto>> getPaginatedEncounters(
            EncounterSearchDto searchDto,
            @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    );

    // --- Elite Tier Clinical Operations ---

    @Operation(
            summary = "Start clinical encounter",
            description = "Transitions from appointment workflow to active encounter.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Encounter started successfully."),
                    @ApiResponse(responseCode = "400", description = "Invalid request."),
                    @ApiResponse(responseCode = "409", description = "Encounter already in progress."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            }
    )
    @PostMapping(value = "/start", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EncounterDto> startEncounter(@Valid @RequestBody StartEncounterRequest request);

//    @Operation(summary = "Poner Encuentro en Espera/Pausa")
//    @PostMapping(value = ID + "/hold", produces = MediaType.APPLICATION_JSON_VALUE)
//    ResponseEntity<EncounterDto> holdEncounter(@PathVariable("id") Long id, @RequestParam String reason);
//
//    @Operation(summary = "Reanudar Encuentro en Espera")
//    @PostMapping(value = ID + "/resume", produces = MediaType.APPLICATION_JSON_VALUE)
//    ResponseEntity<EncounterDto> resumeEncounter(@PathVariable("id") Long id);

    @Operation(
            summary = "Complete encounter",
            description = "Completes encounter. Requires at least one diagnosis.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Encounter completed successfully."),
                    @ApiResponse(responseCode = "400", description = "Missing diagnosis."),
                    @ApiResponse(responseCode = "404", description = "Encounter not found."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            }
    )
    @PostMapping(value = ID + "/complete", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EncounterDto> completeEncounter(@PathVariable("id") Long id);

    @Operation(
            summary = "Cancel encounter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Encounter cancelled."),
                    @ApiResponse(responseCode = "400", description = "Invalid state."),
                    @ApiResponse(responseCode = "404", description = "Encounter not found.")
            }
    )
    @PostMapping(value = ID + "/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EncounterDto> cancelEncounter(@PathVariable("id") Long id,
                                                 @RequestParam String reason);

    @Operation(
            summary = "Add diagnosis (Condition)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Condition added."),
                    @ApiResponse(responseCode = "400", description = "Invalid data."),
                    @ApiResponse(responseCode = "409", description = "Condition already exists.")
            }
    )
    @PostMapping(value = ID + "/conditions", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ConditionDto> addCondition(@PathVariable("id") Long id, @Valid @RequestBody ConditionRequest request);

    @Operation(
            summary = "Add observation (vitals, labs)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Observation added."),
                    @ApiResponse(responseCode = "400", description = "Invalid data.")
            }
    )
    @PostMapping(value = ID + "/observations", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ObservationDto> addObservation(@PathVariable("id") Long id, @Valid @RequestBody ObservationRequest request);

    @Operation(
            summary = "Prescribe medication",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Prescription created."),
                    @ApiResponse(responseCode = "400", description = "Invalid prescription data."),
                    @ApiResponse(responseCode = "404", description = "Encounter not found.")
            }
    )
    @PostMapping(value = ID + "/medications", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> prescribeMedication(@PathVariable("id") Long id, @Valid @RequestBody Prescription request);
}
