package com.amachi.app.vitalia.medicalcatalog.kinship.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcatalog.kinship.dto.KinshipDto;
import com.amachi.app.vitalia.medicalcatalog.kinship.dto.search.KinshipSearchDto;
import com.amachi.app.vitalia.medicalcatalog.kinship.entity.Kinship;
import com.amachi.app.vitalia.medicalcatalog.kinship.mapper.KinshipMapper;
import com.amachi.app.vitalia.medicalcatalog.kinship.service.impl.KinshipServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mdm/kinships")
@RequiredArgsConstructor
public class KinshipController extends BaseController implements KinshipApi {

    private final KinshipServiceImpl service;
    private final KinshipMapper mapper;

    @Override
    public ResponseEntity<KinshipDto> getKinshipById(@NonNull Long id) {
        Kinship entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<KinshipDto> createKinship(@Valid @RequestBody @NonNull KinshipDto dto) {
        Kinship savedEntity = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<KinshipDto> updateKinship(@NonNull Long id, @Valid @RequestBody @NonNull KinshipDto dto) {
        Kinship savedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteKinship(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<KinshipDto>> getAllKinships() {
        List<Kinship> entities = service.getAll();
        List<KinshipDto> dtos = entities.stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<KinshipDto>> getPaginatedKinships(
            @NonNull KinshipSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Kinship> page = service.getAll(searchDto, pageIndex, pageSize);
        List<KinshipDto> dtos = page.getContent().stream().map(mapper::toDto).toList();

        PageResponseDto<KinshipDto> response = PageResponseDto.<KinshipDto>builder()
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
