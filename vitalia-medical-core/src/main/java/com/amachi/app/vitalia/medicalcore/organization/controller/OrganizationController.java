package com.amachi.app.vitalia.medicalcore.organization.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.organization.dto.OrganizationDto;
import com.amachi.app.vitalia.medicalcore.organization.dto.search.OrganizationSearchDto;
import com.amachi.app.vitalia.medicalcore.organization.entity.Organization;
import com.amachi.app.vitalia.medicalcore.organization.mapper.OrganizationMapper;
import com.amachi.app.vitalia.medicalcore.organization.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
@Slf4j
public class OrganizationController extends BaseController implements OrganizationApi {

    private final OrganizationService service;
    private final OrganizationMapper mapper;

    @Override
    public ResponseEntity<OrganizationDto> getOrganizationById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<OrganizationDto> createOrganization(@Valid @RequestBody @NonNull OrganizationDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<OrganizationDto> updateOrganization(@NonNull Long id, @Valid @RequestBody @NonNull OrganizationDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteOrganization(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<OrganizationDto>> getAllOrganizations() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<OrganizationDto>> getPaginatedOrganizations(@NonNull OrganizationSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Organization> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<OrganizationDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
