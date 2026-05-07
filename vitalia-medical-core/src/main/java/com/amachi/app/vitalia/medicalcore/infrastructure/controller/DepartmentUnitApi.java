package com.amachi.app.vitalia.medicalcore.infrastructure.controller;

import com.amachi.app.core.common.controller.GenericApi;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.DepartmentUnitDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.search.DepartmentUnitSearchDto;
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

@Tag(name = "Infrastructure - Department Units", description = "Hospital organizational units: departments, floors, clinical services and administrative areas.")
public interface DepartmentUnitApi extends GenericApi<DepartmentUnitDto> {

    String NAME_API = "DepartmentUnit";

    @Operation(summary = "Get " + NAME_API + " by ID",
        responses = {
            @ApiResponse(responseCode = "200", description = NAME_API + " retrieved successfully."),
            @ApiResponse(responseCode = "404", description = NAME_API + " not found.")
        })
    @GetMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DepartmentUnitDto> getDepartmentUnitById(@PathVariable("id") @NonNull Long id);

    @Operation(summary = "Create a " + NAME_API,
        responses = {
            @ApiResponse(responseCode = "201", description = NAME_API + " created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate code.")
        })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DepartmentUnitDto> createDepartmentUnit(@Valid @RequestBody @NonNull DepartmentUnitDto dto);

    @Operation(summary = "Update a " + NAME_API + " by ID",
        responses = {
            @ApiResponse(responseCode = "200", description = NAME_API + " updated successfully."),
            @ApiResponse(responseCode = "404", description = NAME_API + " not found.")
        })
    @PutMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DepartmentUnitDto> updateDepartmentUnit(@PathVariable("id") @NonNull Long id,
                                                           @Valid @RequestBody @NonNull DepartmentUnitDto dto);

    @Operation(summary = "Delete a " + NAME_API + " by ID",
        responses = {
            @ApiResponse(responseCode = "204", description = NAME_API + " deleted successfully."),
            @ApiResponse(responseCode = "404", description = NAME_API + " not found.")
        })
    @DeleteMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> deleteDepartmentUnit(@PathVariable("id") @NonNull Long id);

    @Operation(summary = "Get all " + NAME_API + "s")
    @GetMapping(value = ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<DepartmentUnitDto>> getAllDepartmentUnits();

    @Operation(summary = "Get paginated " + NAME_API + "s with filters")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PageResponseDto<DepartmentUnitDto>> getPaginatedDepartmentUnits(
            @Parameter(description = "Search filters") @NonNull DepartmentUnitSearchDto searchDto,
            @RequestParam(value = "pageIndex", defaultValue = "0", required = false) Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize);

    @Operation(summary = "Get top-level (root) units with no parent")
    @GetMapping(value = "/roots", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<DepartmentUnitDto>> getRootUnits();

    @Operation(summary = "Get sub-units of a given parent unit")
    @GetMapping(value = "/{parentId}/sub-units", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<DepartmentUnitDto>> getSubUnits(@PathVariable("parentId") @NonNull Long parentId);
}
