package com.amachi.app.core.geography.municipality.controller;

import com.amachi.app.core.common.controller.GenericApi;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.geography.municipality.dto.MunicipalityDto;
import com.amachi.app.core.geography.municipality.dto.search.MunicipalitySearchDto;
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

import com.amachi.app.core.common.controller.BaseController;

@Tag(name = "Municipality", description = "REST API to manage municipality details: create, update, fetch and delete.")
public interface MunicipalityApi extends GenericApi<MunicipalityDto> {

    String NAME_API = "Municipality";

    @Operation(
            summary = "Get a " + NAME_API + " by ID",
            description = "Returns a " + NAME_API + " object by the specified ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = NAME_API + " found successfully."),
                    @ApiResponse(responseCode = "400", description = "Invalid request: Null ID or incomplete data."),
                    @ApiResponse(responseCode = "404", description = NAME_API + " not found."),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            }
    )
    @GetMapping(value = BaseController.ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MunicipalityDto> getMunicipalityById(
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
    @PostMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MunicipalityDto> createMunicipality(
            @Parameter(description = "Details of the " + NAME_API + " to create.", required = true)
            @Valid @RequestBody @NonNull MunicipalityDto dto
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
    @PutMapping(value = BaseController.ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MunicipalityDto> updateMunicipality(
            @Parameter(description = "ID of the " + NAME_API + " to update.", required = true)
            @PathVariable("id") @NonNull Long id,
            @Parameter(description = "New details of the " + NAME_API + ".", required = true)
            @Valid @RequestBody @NonNull MunicipalityDto dto
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
    @DeleteMapping(value = BaseController.ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> deleteMunicipality(
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
    @GetMapping(value = BaseController.ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<MunicipalityDto>> getAllMunicipalities();

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
    ResponseEntity<PageResponseDto<MunicipalityDto>> getPaginatedMunicipalities(
                                                                                @NonNull MunicipalitySearchDto searchDto,
                                                                                @Parameter(description = "Index of the page to retrieve.", example = "0") @RequestParam(value = "pageIndex", defaultValue = "0", required = false) Integer pageIndex,
                                                                                @Parameter(description = "Size of the page.", example = "10") @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
    );
}
