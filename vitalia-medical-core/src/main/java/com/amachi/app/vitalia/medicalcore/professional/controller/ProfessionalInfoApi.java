package com.amachi.app.vitalia.medicalcore.professional.controller;

import com.amachi.app.core.common.controller.GenericApi;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.professional.dto.ProfessionalInfoDto;
import com.amachi.app.vitalia.medicalcore.professional.dto.search.ProfessionalInfoSearchDto;
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

@Tag(name = "ProfessionalInfo", description = "Professional work history by period")
public interface ProfessionalInfoApi extends GenericApi<ProfessionalInfoDto> {
    String NAME_API = "ProfessionalInfo";

    @GetMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get " + NAME_API + " by ID", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    ResponseEntity<ProfessionalInfoDto> getProfessionalInfoById(@PathVariable("id") @NonNull Long id);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create " + NAME_API, responses = {@ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "400")})
    ResponseEntity<ProfessionalInfoDto> createProfessionalInfo(@Valid @RequestBody @NonNull ProfessionalInfoDto dto);

    @PutMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update " + NAME_API, responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")})
    ResponseEntity<ProfessionalInfoDto> updateProfessionalInfo(@PathVariable("id") @NonNull Long id, @Valid @RequestBody @NonNull ProfessionalInfoDto dto);

    @DeleteMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete " + NAME_API, responses = {@ApiResponse(responseCode = "204"), @ApiResponse(responseCode = "404")})
    ResponseEntity<Void> deleteProfessionalInfo(@PathVariable("id") @NonNull Long id);

    @GetMapping(value = ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all " + NAME_API)
    ResponseEntity<List<ProfessionalInfoDto>> getAllProfessionalInfos();

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get paginated " + NAME_API)
    ResponseEntity<PageResponseDto<ProfessionalInfoDto>> getPaginatedProfessionalInfos(
            @NonNull ProfessionalInfoSearchDto searchDto,
            @Parameter(example = "0") @RequestParam(value = "pageIndex", defaultValue = "0", required = false) Integer pageIndex,
            @Parameter(example = "10") @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize);
}
