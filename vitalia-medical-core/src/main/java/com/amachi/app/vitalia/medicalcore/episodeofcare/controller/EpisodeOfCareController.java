package com.amachi.app.vitalia.medicalcore.episodeofcare.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.episodeofcare.dto.EpisodeOfCareDto;
import com.amachi.app.vitalia.medicalcore.episodeofcare.dto.search.EpisodeOfCareSearchDto;
import com.amachi.app.vitalia.medicalcore.episodeofcare.entity.EpisodeOfCare;
import com.amachi.app.vitalia.medicalcore.episodeofcare.mapper.EpisodeOfCareMapper;
import com.amachi.app.vitalia.medicalcore.episodeofcare.service.EpisodeOfCareService;
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
@RequestMapping("/episodes-of-care")
@RequiredArgsConstructor
@Slf4j
public class EpisodeOfCareController extends BaseController implements EpisodeOfCareApi {

    private final EpisodeOfCareService service;
    private final EpisodeOfCareMapper mapper;

    @Override
    public ResponseEntity<EpisodeOfCareDto> getEpisodeOfCareById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<EpisodeOfCareDto> createEpisodeOfCare(@Valid @RequestBody @NonNull EpisodeOfCareDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<EpisodeOfCareDto> updateEpisodeOfCare(@NonNull Long id, @Valid @RequestBody @NonNull EpisodeOfCareDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteEpisodeOfCare(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<EpisodeOfCareDto>> getAllEpisodesOfCare() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<EpisodeOfCareDto>> getPaginatedEpisodesOfCare(@NonNull EpisodeOfCareSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<EpisodeOfCare> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<EpisodeOfCareDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
