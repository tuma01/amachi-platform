package com.amachi.app.vitalia.medicalcore.infrastructure.controller;

import com.amachi.app.core.common.controller.GenericApi;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.common.enums.BedStatusEnum;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.BedDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.search.BedSearchDto;
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

@Tag(name = "Infrastructure - Beds", description = "Hospital bed management: availability, occupancy status and maintenance scheduling.")
public interface BedApi extends GenericApi<BedDto> {

    String NAME_API = "Bed";

    @Operation(summary = "Get " + NAME_API + " by ID",
        responses = {
            @ApiResponse(responseCode = "200", description = NAME_API + " retrieved successfully."),
            @ApiResponse(responseCode = "404", description = NAME_API + " not found.")
        })
    @GetMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BedDto> getBedById(@PathVariable("id") @NonNull Long id);

    @Operation(summary = "Create a " + NAME_API,
        responses = {
            @ApiResponse(responseCode = "201", description = NAME_API + " created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate bed code in room.")
        })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BedDto> createBed(@Valid @RequestBody @NonNull BedDto dto);

    @Operation(summary = "Update a " + NAME_API + " by ID")
    @PutMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BedDto> updateBed(@PathVariable("id") @NonNull Long id,
                                     @Valid @RequestBody @NonNull BedDto dto);

    @Operation(summary = "Delete a " + NAME_API + " by ID")
    @DeleteMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> deleteBed(@PathVariable("id") @NonNull Long id);

    @Operation(summary = "Get all " + NAME_API + "s")
    @GetMapping(value = ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<BedDto>> getAllBeds();

    @Operation(summary = "Get paginated " + NAME_API + "s with filters")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PageResponseDto<BedDto>> getPaginatedBeds(
            @Parameter(description = "Search filters") @NonNull BedSearchDto searchDto,
            @RequestParam(value = "pageIndex", defaultValue = "0", required = false) Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize);

    @Operation(summary = "Get all beds in a specific room")
    @GetMapping(value = "/by-room/{roomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<BedDto>> getBedsByRoom(@PathVariable("roomId") @NonNull Long roomId);

    @Operation(summary = "Update bed operational status (AVAILABLE, OCCUPIED, DIRTY, MAINTENANCE, RESERVED)",
        description = "Transitions the bed status. Setting OCCUPIED also marks isOccupied=true.")
    @PatchMapping(value = ID + "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BedDto> updateBedStatus(
            @PathVariable("id") @NonNull Long id,
            @Parameter(description = "New operational status", required = true)
            @RequestParam("status") @NonNull BedStatusEnum status);
}
