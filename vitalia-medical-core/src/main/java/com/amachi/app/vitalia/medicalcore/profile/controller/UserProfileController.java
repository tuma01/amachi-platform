package com.amachi.app.vitalia.medicalcore.profile.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.profile.dto.UserProfileDto;
import com.amachi.app.vitalia.medicalcore.profile.dto.search.UserProfileSearchDto;
import com.amachi.app.vitalia.medicalcore.profile.entity.UserProfile;
import com.amachi.app.vitalia.medicalcore.profile.mapper.UserProfileMapper;
import com.amachi.app.vitalia.medicalcore.profile.service.UserProfileService;
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
@RequestMapping("/user-profiles")
@RequiredArgsConstructor
@Slf4j
public class UserProfileController extends BaseController implements UserProfileApi {

    private final UserProfileService service;
    private final UserProfileMapper mapper;

    @Override
    public ResponseEntity<UserProfileDto> getUserProfileById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<UserProfileDto> createUserProfile(@Valid @RequestBody @NonNull UserProfileDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserProfileDto> updateUserProfile(@NonNull Long id, @Valid @RequestBody @NonNull UserProfileDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteUserProfile(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<UserProfileDto>> getAllUserProfiles() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<UserProfileDto>> getPaginatedUserProfiles(@NonNull UserProfileSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<UserProfile> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<UserProfileDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
