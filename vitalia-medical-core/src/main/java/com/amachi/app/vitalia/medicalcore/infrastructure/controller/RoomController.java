package com.amachi.app.vitalia.medicalcore.infrastructure.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.common.utils.AppConstants;
import com.amachi.app.vitalia.medicalcore.common.enums.CleaningStatus;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.RoomDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.search.RoomSearchDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Room;
import com.amachi.app.vitalia.medicalcore.infrastructure.mapper.RoomMapper;
import com.amachi.app.vitalia.medicalcore.infrastructure.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(AppConstants.Url.API_V1 + "/rooms")
@RequiredArgsConstructor
public class RoomController extends BaseController implements RoomApi {

    private final RoomService service;
    private final RoomMapper mapper;

    @Override
    public ResponseEntity<RoomDto> getRoomById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<RoomDto> createRoom(@NonNull RoomDto dto) {
        Room saved = service.create(dto);
        return ResponseEntity.created(
                URI.create(AppConstants.Url.API_V1 + "/rooms/" + saved.getId())
        ).body(mapper.toDto(saved));
    }

    @Override
    public ResponseEntity<RoomDto> updateRoom(@NonNull Long id, @NonNull RoomDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteRoom(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<RoomDto>> getAllRooms() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<RoomDto>> getPaginatedRooms(
            @NonNull RoomSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Room> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<RoomDto>builder()
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
    public ResponseEntity<List<RoomDto>> getRoomsByUnit(@NonNull Long unitId) {
        return ResponseEntity.ok(service.getRoomsByUnit(unitId).stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<RoomDto> updateCleaningStatus(@NonNull Long id, @NonNull CleaningStatus status) {
        Room room = service.getById(id);
        room.setCleaningStatus(status);
        // Persist via update with current state
        RoomDto dto = mapper.toDto(room);
        dto.setCleaningStatus(status);
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }
}
