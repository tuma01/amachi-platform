package com.amachi.app.vitalia.medicalcore.infrastructure.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.common.enums.BedStatusEnum;
import com.amachi.app.core.common.utils.AppConstants;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.BedDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.search.BedSearchDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Bed;
import com.amachi.app.vitalia.medicalcore.infrastructure.mapper.BedMapper;
import com.amachi.app.vitalia.medicalcore.infrastructure.service.BedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(AppConstants.Url.API_V1 + "/beds")
@RequiredArgsConstructor
public class BedController extends BaseController implements BedApi {

    private final BedService service;
    private final BedMapper mapper;

    @Override
    public ResponseEntity<BedDto> getBedById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<BedDto> createBed(@NonNull BedDto dto) {
        Bed saved = service.create(dto);
        return ResponseEntity.created(
                URI.create(AppConstants.Url.API_V1 + "/beds/" + saved.getId())
        ).body(mapper.toDto(saved));
    }

    @Override
    public ResponseEntity<BedDto> updateBed(@NonNull Long id, @NonNull BedDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteBed(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<BedDto>> getAllBeds() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<BedDto>> getPaginatedBeds(
            @NonNull BedSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Bed> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<BedDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements())
                .pageIndex(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .numberOfElements(page.getNumberOfElements())
                .build());
    }

    @Override
    public ResponseEntity<List<BedDto>> getBedsByRoom(@NonNull Long roomId) {
        return ResponseEntity.ok(service.getBedsByRoom(roomId).stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<BedDto> updateBedStatus(@NonNull Long id, @NonNull BedStatusEnum status) {
        return ResponseEntity.ok(mapper.toDto(service.updateStatus(id, status)));
    }
}
