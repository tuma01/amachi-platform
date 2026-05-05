package com.amachi.app.vitalia.medicalcatalog.healthcareprovider.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.dto.HealthcareProviderDto;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.dto.search.HealthcareProviderSearchDto;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.entity.HealthcareProvider;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.mapper.HealthcareProviderMapper;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.service.HealthcareProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mdm/healthcare-providers")
@RequiredArgsConstructor
public class HealthcareProviderController extends BaseController implements HealthcareProviderApi {

    private final HealthcareProviderService service;
    private final HealthcareProviderMapper mapper;

    @Override
    public ResponseEntity<HealthcareProviderDto> getProviderById(@NonNull Long id) {
        HealthcareProvider entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<HealthcareProviderDto> createProvider(@Valid @RequestBody @NonNull HealthcareProviderDto dto) {
        HealthcareProvider savedEntity = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<HealthcareProviderDto> updateProvider(@NonNull Long id, @Valid @RequestBody @NonNull HealthcareProviderDto dto) {
        HealthcareProvider savedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteProvider(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<HealthcareProviderDto>> getAllProviders() {
        List<HealthcareProvider> entities = service.getAll();
        List<HealthcareProviderDto> dtos = entities.stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<HealthcareProviderDto>> getPaginatedProviders(
            @NonNull HealthcareProviderSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<HealthcareProvider> page = service.getAll(searchDto, pageIndex, pageSize);
        List<HealthcareProviderDto> dtos = page.getContent().stream().map(mapper::toDto).toList();

        PageResponseDto<HealthcareProviderDto> response = PageResponseDto.<HealthcareProviderDto>builder()
                .content(dtos)
                .totalElements(page.getTotalElements())
                .pageIndex(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .numberOfElements(page.getNumberOfElements())
                .build();

        return ResponseEntity.ok(response);
    }
}
