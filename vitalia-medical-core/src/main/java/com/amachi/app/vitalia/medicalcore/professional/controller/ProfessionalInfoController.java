package com.amachi.app.vitalia.medicalcore.professional.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.professional.dto.ProfessionalInfoDto;
import com.amachi.app.vitalia.medicalcore.professional.dto.search.ProfessionalInfoSearchDto;
import com.amachi.app.vitalia.medicalcore.professional.entity.ProfessionalInfo;
import com.amachi.app.vitalia.medicalcore.professional.mapper.ProfessionalInfoMapper;
import com.amachi.app.vitalia.medicalcore.professional.service.ProfessionalInfoService;
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
@RequestMapping("/professional-infos")
@RequiredArgsConstructor
@Slf4j
public class ProfessionalInfoController extends BaseController implements ProfessionalInfoApi {

    private final ProfessionalInfoService service;
    private final ProfessionalInfoMapper mapper;

    @Override
    public ResponseEntity<ProfessionalInfoDto> getProfessionalInfoById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<ProfessionalInfoDto> createProfessionalInfo(@Valid @RequestBody @NonNull ProfessionalInfoDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ProfessionalInfoDto> updateProfessionalInfo(@NonNull Long id, @Valid @RequestBody @NonNull ProfessionalInfoDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteProfessionalInfo(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ProfessionalInfoDto>> getAllProfessionalInfos() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<ProfessionalInfoDto>> getPaginatedProfessionalInfos(@NonNull ProfessionalInfoSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<ProfessionalInfo> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<ProfessionalInfoDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
