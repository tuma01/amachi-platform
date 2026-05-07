package com.amachi.app.vitalia.medicalcore.doctor.controller;

import com.amachi.app.core.common.controller.GenericApi;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.doctor.dto.DoctorHospitalAssignmentDto;
import com.amachi.app.vitalia.medicalcore.doctor.dto.search.DoctorHospitalAssignmentSearchDto;
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

@Tag(name = "DoctorHospitalAssignment", description = "Doctor-to-hospital assignment management")
public interface DoctorHospitalAssignmentApi extends GenericApi<DoctorHospitalAssignmentDto> {
    String NAME_API = "DoctorHospitalAssignment";

    @GetMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get " + NAME_API + " by ID", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    ResponseEntity<DoctorHospitalAssignmentDto> getDoctorHospitalAssignmentById(@PathVariable("id") @NonNull Long id);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create " + NAME_API, responses = {@ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "400")})
    ResponseEntity<DoctorHospitalAssignmentDto> createDoctorHospitalAssignment(@Valid @RequestBody @NonNull DoctorHospitalAssignmentDto dto);

    @PutMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update " + NAME_API, responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    ResponseEntity<DoctorHospitalAssignmentDto> updateDoctorHospitalAssignment(@PathVariable("id") @NonNull Long id, @Valid @RequestBody @NonNull DoctorHospitalAssignmentDto dto);

    @DeleteMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete " + NAME_API, responses = {@ApiResponse(responseCode = "204"), @ApiResponse(responseCode = "404")})
    ResponseEntity<Void> deleteDoctorHospitalAssignment(@PathVariable("id") @NonNull Long id);

    @GetMapping(value = ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all " + NAME_API)
    ResponseEntity<List<DoctorHospitalAssignmentDto>> getAllDoctorHospitalAssignments();

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get paginated " + NAME_API)
    ResponseEntity<PageResponseDto<DoctorHospitalAssignmentDto>> getPaginatedDoctorHospitalAssignments(
            @NonNull DoctorHospitalAssignmentSearchDto searchDto,
            @Parameter(example = "0") @RequestParam(value = "pageIndex", defaultValue = "0", required = false) Integer pageIndex,
            @Parameter(example = "10") @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize);
}
