package com.amachi.app.vitalia.medicalcore.infrastructure.controller;

import com.amachi.app.core.common.controller.GenericApi;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.common.enums.CleaningStatus;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.RoomDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.search.RoomSearchDto;
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

@Tag(name = "Infrastructure - Rooms", description = "Hospital rooms, consulting rooms and boxes assigned to department units.")
public interface RoomApi extends GenericApi<RoomDto> {

    String NAME_API = "Room";

    @Operation(summary = "Get " + NAME_API + " by ID",
        responses = {
            @ApiResponse(responseCode = "200", description = NAME_API + " retrieved successfully."),
            @ApiResponse(responseCode = "404", description = NAME_API + " not found.")
        })
    @GetMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<RoomDto> getRoomById(@PathVariable("id") @NonNull Long id);

    @Operation(summary = "Create a " + NAME_API,
        responses = {
            @ApiResponse(responseCode = "201", description = NAME_API + " created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate room number in unit.")
        })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<RoomDto> createRoom(@Valid @RequestBody @NonNull RoomDto dto);

    @Operation(summary = "Update a " + NAME_API + " by ID",
        responses = {
            @ApiResponse(responseCode = "200", description = NAME_API + " updated successfully."),
            @ApiResponse(responseCode = "404", description = NAME_API + " not found.")
        })
    @PutMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<RoomDto> updateRoom(@PathVariable("id") @NonNull Long id,
                                       @Valid @RequestBody @NonNull RoomDto dto);

    @Operation(summary = "Delete a " + NAME_API + " by ID")
    @DeleteMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> deleteRoom(@PathVariable("id") @NonNull Long id);

    @Operation(summary = "Get all " + NAME_API + "s")
    @GetMapping(value = ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<RoomDto>> getAllRooms();

    @Operation(summary = "Get paginated " + NAME_API + "s with filters")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PageResponseDto<RoomDto>> getPaginatedRooms(
            @Parameter(description = "Search filters") @NonNull RoomSearchDto searchDto,
            @RequestParam(value = "pageIndex", defaultValue = "0", required = false) Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize);

    @Operation(summary = "Get all rooms belonging to a department unit")
    @GetMapping(value = "/by-unit/{unitId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<RoomDto>> getRoomsByUnit(@PathVariable("unitId") @NonNull Long unitId);

    @Operation(summary = "Update cleaning status of a room")
    @PatchMapping(value = ID + "/cleaning-status", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<RoomDto> updateCleaningStatus(@PathVariable("id") @NonNull Long id,
                                                  @RequestParam("status") @NonNull CleaningStatus status);
}
