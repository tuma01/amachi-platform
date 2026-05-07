package com.amachi.app.vitalia.medicalcore.hospitalization.controller;

import com.amachi.app.core.common.controller.GenericApi;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.HospitalizationDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.search.HospitalizationSearchDto;
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

@Tag(name = "Hospitalization", description = "Gestión de episodios de internación hospitalaria")
public interface HospitalizationApi extends GenericApi<HospitalizationDto> {
    String NAME_API = "Hospitalization";

    @GetMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get " + NAME_API + " by ID", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    ResponseEntity<HospitalizationDto> getHospitalizationById(@PathVariable("id") @NonNull Long id);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Admit patient — marks bed as OCCUPIED", responses = {@ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "400")})
    ResponseEntity<HospitalizationDto> createHospitalization(@Valid @RequestBody @NonNull HospitalizationDto dto);

    @PutMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update " + NAME_API, responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    ResponseEntity<HospitalizationDto> updateHospitalization(@PathVariable("id") @NonNull Long id, @Valid @RequestBody @NonNull HospitalizationDto dto);

    @DeleteMapping(value = ID)
    @Operation(summary = "Delete " + NAME_API + " — releases bed", responses = {@ApiResponse(responseCode = "204"), @ApiResponse(responseCode = "404")})
    ResponseEntity<Void> deleteHospitalization(@PathVariable("id") @NonNull Long id);

    @GetMapping(value = ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all " + NAME_API)
    ResponseEntity<List<HospitalizationDto>> getAllHospitalizations();

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get paginated " + NAME_API)
    ResponseEntity<PageResponseDto<HospitalizationDto>> getPaginatedHospitalizations(
            @NonNull HospitalizationSearchDto searchDto,
            @Parameter(example = "0") @RequestParam(defaultValue = "0", required = false) Integer pageIndex,
            @Parameter(example = "10") @RequestParam(defaultValue = "10", required = false) Integer pageSize);

    @GetMapping(value = "/patient/{patientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all hospitalizations for a patient")
    ResponseEntity<List<HospitalizationDto>> getHospitalizationsByPatient(@PathVariable @NonNull Long patientId);

    @PatchMapping(value = "/{id}/discharge", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Discharge patient — sets discharge date and releases bed")
    ResponseEntity<HospitalizationDto> dischargePatient(@PathVariable @NonNull Long id,
                                                        @RequestParam(required = false) String dischargeSummary);
}
